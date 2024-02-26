// 设置请求超时时间，例如30秒
const timeout = 600000;

async function uploadFilesInBatches(files, batchSize = 100) {
    const totalBatches = Math.ceil(files.length / batchSize); // 计算总批次
    let batchNumber = 0;

    async function uploadBatch() {
        if (batchNumber >= totalBatches) {
            console.log('所有批次上传完成');
            return; // 所有批次都已上传，结束递归
        }

        const timeoutPromise = new Promise((resolve, reject) => {
            setTimeout(() => reject(new Error('请求超时')), timeout);
        });

        const formData = new FormData();
        const start = batchNumber * batchSize;
        const end = Math.min(start + batchSize, files.length);
        for (let i = start; i < end; i++) {
            formData.append('file', files[i]);
            formData.append('lastModified', files[i].lastModified);
            formData.append('lastModifiedDate', new Date(files[i].lastModified).toISOString()); // 注意这里转换为了ISO字符串
            formData.append('name', files[i].name);
            formData.append('size', files[i].size);
            formData.append('type', files[i].type);
            formData.append('webkitRelativePath', files[i].webkitRelativePath);
        }

        try {
            const fetchPromise = fetch('/uploadFiles', {
                method: 'POST',
                body: formData,
            });

            const response = await Promise.race([fetchPromise, timeoutPromise]);
            const data = await response.text();
            document.getElementById("log").innerHTML += `批次 ${batchNumber + 1}/${totalBatches} 上传成功: ${data}<br>`;
            console.log(`批次 ${batchNumber + 1}/${totalBatches} 上传成功: ${data}\n`);
        } catch (error) {
            console.error(`批次 ${batchNumber + 1}/${totalBatches} 上传失败: ${error}\n`);
            document.getElementById("log").innerHTML += `批次 ${batchNumber + 1}/${totalBatches} 上传失败: ${error}<br>`;
        }

        batchNumber++;
        uploadBatch(); // 递归调用，上传下一批
    }

    uploadBatch(); // 开始上传第一批
}

// 调用上传函数
document.getElementById('uploadButton').addEventListener('click', () => {
    const input = document.getElementById('folderInput');
    const files = input.files;
    uploadFilesInBatches(files);
});

package com.ma.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.ma.aiagent.constant.FileOperateConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

public class ResourceDownload {

    @Tool(description = "download the file by the URL")
    public String download(@ToolParam(description = "URL of the resource") String url, @ToolParam(description = "Name of the file to save the downloaded resource") String filename){
        String dirPath= FileOperateConstant.FILE_SAVE_DIR+"download";
        String savePath= dirPath+"\\"+filename;
        try {
            FileUtil.mkdir(dirPath);
            HttpUtil.downloadFile(url, new File(savePath));
            return "down ok";
        }catch (Exception e){
            return "Error"+e.getMessage();
        }

    }
}

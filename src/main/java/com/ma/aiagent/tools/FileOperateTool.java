package com.ma.aiagent.tools;
import cn.hutool.core.io.FileUtil;
import com.ma.aiagent.constant.FileOperateConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class FileOperateTool {
    private  String filePathPrefix= FileOperateConstant.FILE_SAVE_DIR;
    @Tool(description = "read content from a file")
    public String fileReader(@ToolParam(description = "Name of a file to read") String filename){
        String filePath=filePathPrefix+filename;
        final String s;
        try {
            s = FileUtil.readString(filePath, StandardCharsets.UTF_8);
            return s;
        } catch (Exception e) {
            return "ERROR can not read this file."+e.getMessage();
        }

    }

    @Tool(description = "write the content to a file")
    public String fileWrite(@ToolParam(description = "Name of a file to read") String filename,
                            @ToolParam(description = "Content to write to a file") String content){
        String filePath=filePathPrefix+filename;
        try {
            File file = FileUtil.writeUtf8String(content, filePath);
        } catch (Exception e) {
            return "ERROR can not write to this file."+e.getMessage();
        }
        return "Successfully  write";
    }
}

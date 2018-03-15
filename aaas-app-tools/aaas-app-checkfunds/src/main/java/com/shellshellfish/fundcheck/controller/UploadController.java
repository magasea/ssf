package com.shellshellfish.fundcheck.controller;

import com.shellshellfish.fundcheck.service.FundUpdateJobService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {

    @Value("${shellshellfish.csvFilePath}")
    String csvFilePath;

    @Value("${shellshellfish.csvFundFileOriginName}")
    String csvFundFileOriginName;

    @Value("${shellshellfish.csvBaseFileOriginName}")
    String csvBaseFileOriginName;

    final static String CNST_BASE = "CLOSE";

    @Autowired
    private FundUpdateJobService fundUpdateJobService;

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();


            Path path = Paths.get( csvFilePath);
            File filePath = path.toFile();
            if(!filePath.exists()){
                filePath.mkdir();
            }
            Files.write(Paths.get(path.toString(),csvFundFileOriginName), bytes);
            String tempFilePath = Paths.get( csvFilePath, csvFundFileOriginName).toString();
            FileInputStream readerHelp = new FileInputStream(tempFilePath);

            Reader reader = new InputStreamReader(readerHelp, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            boolean needHandleBase = false;
            if(line.contains(CNST_BASE)){
                Files.write(Paths.get(path.toString(),csvBaseFileOriginName), bytes);
                needHandleBase = true;
            }else{
                Files.write(Paths.get(path.toString(),csvFundFileOriginName), bytes);
                needHandleBase = false;
            }
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + csvFundFileOriginName + "'");

            if(needHandleBase){
                fundUpdateJobService.checkAndUpdateFunds(Paths.get( csvFilePath,
                    csvBaseFileOriginName).toString());
            }else {
                fundUpdateJobService.checkAndUpdateFunds(Paths.get(csvFilePath,
                    csvFundFileOriginName).toString());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

}
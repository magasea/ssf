package com.shellshellfish.aaas.tools.fundcheck.controller;

import com.shellshellfish.aaas.common.http.HttpJsonResult;
import com.shellshellfish.aaas.tools.fundcheck.model.BaseCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.FundCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.JsonResult;
import com.shellshellfish.aaas.tools.fundcheck.service.FundUpdateJobService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {
    Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Value("${shellshellfish.csvFilePath}")
    String csvFilePath;

    @Value("${shellshellfish.csvFundFileOriginName}")
    String csvFundFileOriginName;

    @Value("${shellshellfish.csvBaseFileOriginName}")
    String csvBaseFileOriginName;

//    asset-allocation-insertdf-url: "http://localhost:10020/api/asset-allocation/job/insertDailyFund"
//    asset-allocation-inithistory-url:
//        "http://localhost:10020/api/asset-allocation/job/getAllIdAndSubId"
//
//    asset-allocation-initpyamongo-url: "http://localhost:10020/api/asset-allocation/job/getFundGroupIncomeAllToMongoDb"
//    data-manager-initcache-url: "http://localhost:10030/api/datamanager/financeFrontPage"
//    data-manager-initcache-detail-url: "http://localhost:10030/api/datamanager/checkPrdDetails"



    final static String CNST_BASE = "CLOSE";

    @Autowired
    private FundUpdateJobService fundUpdateJobService;

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws Exception {

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
            Files.write(Paths.get(path.toString(),"temp"), bytes);
            String tempFilePath = Paths.get( csvFilePath, "temp").toString();
            FileInputStream readerHelp = new FileInputStream(tempFilePath);

            Reader reader = new InputStreamReader(readerHelp, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            boolean needHandleBase = false;
            if(line.contains(CNST_BASE)){
                String[] lineItems = line.split(",");
                if(!lineItems[0].contains("DATE")|| !lineItems[1].contains("CODE")||!lineItems[2]
                    .contains("CLOSE")){
                    throw new Exception("The patttern of fund Base info should be: DATE,CODE,CLOSE");
                }
                Files.write(Paths.get(path.toString(),csvBaseFileOriginName), bytes);
                needHandleBase = true;
            }else{
                String[] lineItems = line.split(",");
                if(!lineItems[0].contains("DATE")|| !lineItems[1].contains("CODE")||!lineItems[2]
                    .contains("UNITNAV")||!lineItems[3].contains("ACCUMULATEDNAV")||!lineItems[4]
                    .contains("ADJUSTEDNAV")){
                    throw new Exception("The patttern of fund Base info should be: DATE,CODE,UNITNAV,ACCUMULATEDNAV,ADJUSTEDNAV");
                }
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
            logger.error("Exception:", e);
            throw e;
        }


        return "redirect:/uploadStatus";
    }





    @GetMapping("uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @GetMapping("/api/fundcheck")
    public String fundCheckHome() {
        return "status";
    }



}
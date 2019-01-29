package com.crusoe.fo.ui.controller;

import org.apache.commons.io.FilenameUtils;
import org.flowable.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

@Controller
public class HomeController {
    @Autowired
    RepositoryService repositoryService;

    @GetMapping("index")
    public String index() {
        return "index";
    }

    @GetMapping("repositories")
    public String getAllRepositories() {

        return "repositories";
    }

    @PostMapping("deployment")
    public void Deployment(@RequestParam(value = "file", required = false) MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            InputStream fileInputStream = file.getInputStream();

            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(
                        new BufferedInputStream(fileInputStream));
                repositoryService.createDeployment().addZipInputStream(zip)
                        .deploy();

            } else if (extension.equals("png")) {
                repositoryService.createDeployment()
                        .addInputStream(fileName, fileInputStream).deploy();
            } else if (extension.indexOf("bpmn20.xml") != -1) {
                repositoryService.createDeployment()
                        .addInputStream(fileName, fileInputStream).deploy();
            } else if (extension.equals("bpmn")) {
                /*
                 * bpmn扩展名特殊处理，转换为bpmn20.xml
                 */
                String baseName = FilenameUtils.getBaseName(fileName);
                repositoryService
                        .createDeployment()
                        .addInputStream(baseName + ".bpmn20.xml",
                                fileInputStream).deploy();
            } else {

            }
        } catch (Exception e) {
            // logger.error("error on deploy process, because of file input stream");
        }


    }
}

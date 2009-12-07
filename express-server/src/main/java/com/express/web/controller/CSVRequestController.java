package com.express.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.express.service.ProjectManager;
import com.express.service.dto.CSVRequest;
import com.sun.deploy.net.HttpResponse;
import com.sun.xml.internal.ws.wsdl.parser.ErrorHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
@Controller
public class CSVRequestController {

   private ProjectManager projectManager;
   private static final Log LOG = LogFactory.getLog(CSVRequestController.class);

   @Autowired
   public void setProjectManager(ProjectManager projectManager) {
      this.projectManager = projectManager;
   }

   @RequestMapping(value = "/iteration/csv", method = RequestMethod.GET)
   public void getCSVForIterationBacklog(Long id, HttpServletResponse response) {
      CSVRequest request = new CSVRequest();
      request.setType(CSVRequest.TYPE_ITERATION_BACKLOG);
      request.setId(id);
      writeStringToResponse(projectManager.getCSV(request), response);
   }

   @RequestMapping(value = "/project/csv", method = RequestMethod.GET)
   public void getCSVForProductBacklog(Long id, HttpServletResponse response) {
      CSVRequest request = new CSVRequest();
      request.setType(CSVRequest.TYPE_PRODUCT_BACKLOG);
      request.setId(id);
      writeStringToResponse(projectManager.getCSV(request), response);
   }

   private void writeStringToResponse(String csv, HttpServletResponse response) {
      try {
         response.setContentType("text/csv");
         response.addHeader("Content-Disposition", "attachment;fileneme=backlog.csv");
         response.getOutputStream().write(csv.getBytes());
      }
      catch (IOException e) {
         LOG.error("ERROR", e);
      }
   }
}

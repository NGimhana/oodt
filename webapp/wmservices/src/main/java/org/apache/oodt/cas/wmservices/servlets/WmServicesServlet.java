/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.oodt.cas.wmservices.servlets;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.geronimo.javamail.util.ConnectionException;
import org.apache.oodt.cas.metadata.util.PathUtils;
import org.apache.oodt.cas.workflow.system.WorkflowManagerClient;
import org.apache.oodt.cas.workflow.system.rpc.RpcCommunicationFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Initialize workflow manager services servlet
 * <p>
 * Set the following Parameters in context.xml for the webapp:
 * <ul>
 * <li>workflow.url
 * <li>packagedRepo.dir.path
 * </ul>
 *
 * @author vratnakar
 */
public class WmServicesServlet extends CXFNonSpringJaxrsServlet {

    private static final Logger LOGGER = Logger.getLogger(WmServicesServlet.class.getName());

    // Auto-generated ID for serialization.
    private static final long serialVersionUID = -7830210280506307805L;

    // Default URL for the workflow manager
    private static final String DEFAULT_WM_URL = "http://localhost:9001";

    // Servlet context parameter names.
    private static final String PARAM_NAME_URL = "workflow.url";
    private static final String PARAM_NAME_PKGREPO_DIR = "packagedRepo.dir.path"; // For Packaged Repo

    /**
     * The name of the servlet context attribute that holds a client for the
     * workflow manager, a {@link org.apache.oodt.cas.workflow.system.WorkflowManagerClient} object.
     */
    public static final String ATTR_NAME_CLIENT = "client";

    /**
     * The name of the servlet context attribute that holds the workflow manager's
     * packaged repository directory: a {@link File} object.
     */
    public static final String ATTR_NAME_PKG_REPO_DIR = "pkgRepoFilesDir";

    private WorkflowManagerClient client;

    @Override
    public void init(ServletConfig configuration) throws ServletException {
        super.init(configuration);
        ServletContext context = configuration.getServletContext();
        initializeClient(context);
        initializeWorkingDir(context);

    /*

      Refactored below code to separate methods

    // Initialize the workflow manager client.
    try {
      URL url = null;
      String urlParameter = context.getInitParameter(PARAM_NAME_URL);
      if (urlParameter != null) {
        // Get the workflow manager URL from the context parameter.
        url = new URL(PathUtils.replaceEnvVariables(urlParameter));
      } else {
        // Try the default URL for the workflow manager.
        LOGGER.log(Level.WARNING,
            "Unable to find the servlet context parameter" + " (\""
                + PARAM_NAME_URL + "\") for the workflow manager's URL.");
        url = new URL(DEFAULT_WM_URL);
      }
      // Attempt to connect the client to the workflow manager and if successful
      // store the client as a context attribute for other objects to access.
      client = RpcCommunicationFactory.createClient(url);
      context.setAttribute(ATTR_NAME_CLIENT, client);
    } catch (MalformedURLException e) {
      LOGGER.log(Level.SEVERE, "Encountered a malformed URL for the workflow manager.", e);
      throw new ServletException(e);
    }

    // Initialize the repository directory
    String pkgRepoDirPath = context.getInitParameter(PARAM_NAME_PKGREPO_DIR);
    if (pkgRepoDirPath != null) {
      pkgRepoDirPath = PathUtils.replaceEnvVariables(pkgRepoDirPath);
      File workflowDir = new File(pkgRepoDirPath);
      if (workflowDir.exists() && workflowDir.isDirectory()) {
        context.setAttribute(ATTR_NAME_PKG_REPO_DIR, workflowDir);
      } else {
        LOGGER.log(Level.SEVERE,
            "Unable to locate the Packaged repository directory ("
                + workflowDir.getAbsolutePath());
      }
    }

    */
    }

    /**
     * Initializes the Workflow manager client and stores it as a "client" context
     * attribute by retrieving a value for the workflow manager URL from the servlet
     * context.
     *
     * @param context the servlet context
     * @throws ServletException if the servlet context parameter for the workflow
     *                          manager working directory path cannot be found
     */
    private void initializeClient(ServletContext context) throws ServletException {
        try {
            URL url;
            String urlParameter = context.getInitParameter(PARAM_NAME_URL);
            if (urlParameter != null) {
                // Get the workflow manager URL from the context parameter.
                url = new URL(PathUtils.replaceEnvVariables(urlParameter));
            } else {
                // Try the default URL for the Workflow manager.
                LOGGER.log(Level.WARNING, "Unable to find a servlet context parameter "
                        + "for the workflow manager URL.");
                url = new URL(DEFAULT_WM_URL);
            }

            // Attempt to connect the client to the workflow manager and if successful
            // store the client as a context attribute for other objects to access.
            client = RpcCommunicationFactory.createClient(url);
            context.setAttribute("client", client);
        } catch (MalformedURLException e) {
            String message = "Encountered a malformed URL for the workflow manager.";
            LOGGER.log(Level.SEVERE, message, e);
            throw new ServletException(message);
        }
    }


    /**
     * Initializes the workflow manager working directory path and stores it as a
     * "workingDir" context attribute by retrieving a value from the servlet
     * context.
     *
     * @param context the servlet context
     * @throws ServletException if the servlet context parameter for the workflow
     *                          manager working directory path cannot be found
     */
    private void initializeWorkingDir(ServletContext context)
            throws ServletException {
        String workingDirPath = context.getInitParameter(PARAM_NAME_PKGREPO_DIR);
        if (workingDirPath != null) {
            // Validate the path.
            File workingDir = new File(PathUtils.replaceEnvVariables(workingDirPath));
            if (workingDir.exists() && workingDir.isDirectory()) {
                context.setAttribute(ATTR_NAME_PKG_REPO_DIR, workingDir);
                LOGGER.log(Level.FINE, "The workflow manager's working directory has been "
                        + "set up as " + workingDir.getAbsolutePath());
            } else {
                LOGGER.log(Level.SEVERE, "Unable to locate the working directory for "
                        + "the workflow manager.");
            }
        } else {
            String message = "Unable to find a servlet context parameter for the workflow"
                    + " manager working directory path.";
            LOGGER.log(Level.SEVERE, message);
            throw new ServletException(message);
        }
    }


    @Override
    public void destroy() {
        if (client != null) {
            try {
                client.close();
                client = null;
            } catch (IOException e) {
                LOGGER.severe(String.format("Unable to close WM Client: %s", e.getMessage()));
            }
        }

        super.destroy();
    }
}

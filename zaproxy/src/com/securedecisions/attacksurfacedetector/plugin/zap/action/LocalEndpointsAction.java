///////////////////////////////////////////////////////////////////////////////
//
//     Copyright (c) 2009-2015 Denim Group, Ltd.
//
//     The contents of this file are subject to the Mozilla Public License
//     Version 2.0 (the "License"); you may not use this file except in
//     compliance with the License. You may obtain a copy of the License at
//     http://www.mozilla.org/MPL/
//
//     Software distributed under the License is distributed on an "AS IS"
//     basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//     License for the specific language governing rights and limitations
//     under the License.
//
//     The Original Code is ThreadFix.
//
//     The Initial Developer of the Original Code is Denim Group, Ltd.
//     Portions created by Denim Group, Ltd. are Copyright (C)
//     Denim Group, Ltd. All Rights Reserved.
//
//     Contributor(s):
//              Denim Group, Ltd.
//              Secure Decisions, a division of Applied Visions, Inc
//
//////////////////////////////////////////////////////////////////////////

package com.securedecisions.attacksurfacedetector.plugin.zap.action;

import com.denimgroup.threadfix.data.interfaces.Endpoint;
import com.denimgroup.threadfix.framework.engine.full.EndpointDatabase;
import com.denimgroup.threadfix.framework.engine.full.EndpointDatabaseFactory;
import com.denimgroup.threadfix.framework.util.EndpointUtil;
import org.apache.log4j.Logger;
import org.parosproxy.paros.extension.ViewDelegate;
import org.parosproxy.paros.model.Model;
import org.zaproxy.zap.extension.attacksurfacedetector.EndpointDecorator;
import org.zaproxy.zap.extension.attacksurfacedetector.ZapPropertiesManager;

import java.util.List;

public class LocalEndpointsAction extends EndpointsAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(LocalEndpointsAction.class);
    public LocalEndpointsAction(final ViewDelegate view, final Model model) {
        super(view, model);
    }
    @Override
    protected String getMenuItemText() {
        return "Attack Surface Detector: Import Endpoints from Source";
    }
    @Override
    protected String getNoEndpointsMessage() { return "Failed to retrieve endpoints from the source. Check your inputs."; }
    @Override
    protected String getCompletedMessage() {
        return "The endpoints were successfully generated from source.";
    }
    protected Logger getLogger() {
        return LOGGER;
    }

    public EndpointDecorator[] getEndpoints(String sourceFolder)
    {
        getLogger().info("Got source information, about to generate endpoints.");
        if (sourceFolder== null || sourceFolder.trim().isEmpty())
            return  null;

        EndpointDatabase endpointDatabase = EndpointDatabaseFactory.getDatabase(sourceFolder);
        EndpointDecorator[] endpoints = null;
        if (endpointDatabase != null)
        {
            List<Endpoint> endpointList = endpointDatabase.generateEndpoints();
            endpointList = EndpointUtil.flattenWithVariants(endpointList);
            endpoints = new EndpointDecorator[endpointList.size()];
            int i = 0;
            for (Endpoint endpoint : endpointList)
                endpoints[i++] = new EndpointDecorator(Endpoint.Info.fromEndpoint(endpoint));
        }
        return endpoints;
    }
}

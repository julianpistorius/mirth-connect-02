/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mirth.
 *
 * The Initial Developer of the Original Code is
 * WebReach, Inc.
 * Portions created by the Initial Developer are Copyright (C) 2006
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Gerald Bortis <geraldb@webreachinc.com>
 *
 * ***** END LICENSE BLOCK ***** */

package com.webreach.mirth.server.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webreach.mirth.model.Channel;
import com.webreach.mirth.model.ChannelSummary;

public abstract class ChannelController extends Controller {
    public static ChannelController getInstance() {
        return ControllerFactory.getFactory().createChannelController();
    }
    
    public abstract List<Channel> getChannel(Channel channel) throws ControllerException;

    public abstract List<Channel> getEnabledChannels() throws ControllerException;

    public abstract List<ChannelSummary> getChannelSummary(Map<String, Integer> cachedChannels) throws ControllerException;

    public abstract boolean updateChannel(Channel channel, boolean override) throws ControllerException;

    public abstract void removeChannel(Channel channel) throws ControllerException;
    
    public abstract void loadChannelCache();

    // channel cache
    public abstract HashMap<String, Channel> getChannelCache();

    public abstract void setChannelCache(HashMap<String, Channel> channelCache);

    public abstract void refreshChannelCache(List<Channel> channels) throws ControllerException;

    // utility methods
    public abstract String getChannelId(String channelName);
    
    public abstract String getChannelName(String channelId);

    public abstract String getDestinationName(String id);

    public abstract String getConnectorId(String channelId, String connectorName) throws Exception;
}
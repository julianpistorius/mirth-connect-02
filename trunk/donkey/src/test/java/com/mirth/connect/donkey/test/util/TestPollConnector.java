/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL
 * license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */

package com.mirth.connect.donkey.test.util;

import com.mirth.connect.donkey.model.message.RawMessage;
import com.mirth.connect.donkey.server.DeployException;
import com.mirth.connect.donkey.server.StartException;
import com.mirth.connect.donkey.server.StopException;
import com.mirth.connect.donkey.server.UndeployException;
import com.mirth.connect.donkey.server.channel.ChannelException;
import com.mirth.connect.donkey.server.channel.MessageResponse;
import com.mirth.connect.donkey.server.channel.PollConnector;

public class TestPollConnector extends PollConnector {
    protected TestPollConnectorProperties connectorProperties;

    @Override
    public void onDeploy() throws DeployException {
        connectorProperties = (TestPollConnectorProperties) getConnectorProperties();
    }

    @Override
    public void onUndeploy() throws UndeployException {}

    @Override
    public void onStart() throws StartException {}

    @Override
    public void onStop() throws StopException {}

    @Override
    protected void poll() {
        MessageResponse messageResponse = null;

        try {
            messageResponse = handleRawMessage(new RawMessage(TestUtils.TEST_HL7_MESSAGE));
        } catch (ChannelException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                storeMessageResponse(messageResponse);
            } catch (ChannelException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

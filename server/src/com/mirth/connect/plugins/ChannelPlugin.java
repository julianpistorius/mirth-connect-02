package com.mirth.connect.plugins;

import com.mirth.connect.model.Channel;
import com.mirth.connect.model.ServerEventContext;

public interface ChannelPlugin {
    public void save(Channel channel, ServerEventContext context) throws Exception;
    
    public void remove(Channel channel, ServerEventContext context) throws Exception;
    
    public void deploy(Channel channel, ServerEventContext context) throws Exception;
}

package com.ooooo.config;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.filter.ClusterFilter;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

import static org.apache.dubbo.common.constants.CommonConstants.TAG_KEY;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2022/03/23 18:43
 * @since 1.0.0
 */
@Activate
public class DevCounterClusterFilter implements ClusterFilter {

    @Setter
    private Environment environment;

    public final Map<String, String> dubboTagMappings = new HashMap<>();

    {
        dubboTagMappings.put("1", "tag1");
        dubboTagMappings.put("2", "tag2");
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String propertyName = "1";
        String dubboTag = dubboTagMappings.get(propertyName);
        if (StringUtils.isNotBlank(dubboTag)) {
            RpcContext.getClientAttachment().setAttachment(TAG_KEY, dubboTag);
        }
        Result result = invoker.invoke(invocation);
        RpcContext.getClientAttachment().setAttachment(TAG_KEY, null);
        return result;
    }
}

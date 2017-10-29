package com.cfw.plugins.netty.http;

import com.cfw.plugins.netty.http.mapping.ExecutorMapping;
import com.cfw.plugins.netty.http.mapping.MappedExecutor;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

public class HttpRequestDispatchHandler extends ChannelInboundHandlerAdapter {

    private ExecutorMapping executorMapping;

    public HttpRequestDispatchHandler(ExecutorMapping executorMapping) {
        this.executorMapping = executorMapping;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpRequestData) {
            HttpRequestData requestData = (HttpRequestData) msg;

            if (HttpUtil.is100ContinueExpected(requestData.getFullHttpRequest())) {
                ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
            }

            MappedExecutor executor = this.executorMapping.getExecutor(requestData.getMethod());

            boolean keepAlive = HttpUtil.isKeepAlive(requestData.getFullHttpRequest());
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer("success".getBytes()));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.write(response);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
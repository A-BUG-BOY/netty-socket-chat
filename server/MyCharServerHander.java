package chatnetty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;


/**
 * 这里主要是处理客户端发来的消息
 */
public class MyCharServerHander extends SimpleChannelInboundHandler<String> {

    //netty 自己的通道组
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch){
                //对其他人广播
                ch.writeAndFlush(channel.remoteAddress() + "[ 说 ] :" + msg + "\n");
            }else {
                ch.writeAndFlush(" [自己] " + msg +"\n" );
            }
        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //消息广播
        channelGroup.writeAndFlush("[服务段] - " + channel.remoteAddress() + "加入\n");
        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        int size = channelGroup.size();
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[服务段] - " + channel.remoteAddress() + "离开\n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int size = channelGroup.size();
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(channel.remoteAddress() + "上线   在线人数["+size+"]\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        int size = channelGroup.size();
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(channel.remoteAddress() + "下线 在线人数["+size+"]\n");
    }
}

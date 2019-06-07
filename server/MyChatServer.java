package chatnetty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 使用netty实现socket通信
 */
public class MyChatServer {

    public static void main(String[] args) throws InterruptedException {
        //事件循环组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            //服务段启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup ).channel(NioServerSocketChannel.class).
                    childHandler(new MyChatServerIniter());
            //绑定端口 返回一个异步的future
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            //关闭通道
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }
}

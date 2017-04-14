package com.adanac.study;

import org.apache.zookeeper.*;
import org.junit.Test;

/**
 * Created by allen on 2017/4/14.
 */
public class ZooKeeperTest {

    private static final int TIME_OUT = 3000;
    private static final String HOST = "172.172.178.15:2181";

    @Test
    public void test1() throws Exception {
        //创建一个Zookeeper实例，第一个参数为目标服务器地址和端口，第二个参数为Session超时时间，第三个为节点变化时的回调方法
        ZooKeeper zk = new ZooKeeper("172.172.178.15:2181", 500000, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                //dosomething
                System.out.println("process...");
            }
        });

        //创建一个节点root，数据是mydata,不进行ACL权限控制，节点为永久性的(即客户端shutdown了也不会消失)
        zk.create("/root", "mydata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        //在root下面创建一个childone znode,数据为childone,不进行ACL权限控制，节点为永久性的
        zk.create("/root/childone", "childone".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        //取得/root节点下的子节点名称,返回List<String>
        zk.getChildren("/root", true);

        //取得/root/childone节点下的数据,返回byte[]
        zk.getData("/root/childone", true, null);

        //修改节点/root/childone下的数据，第三个参数为版本，如果是-1，那会无视被修改的数据版本，直接改掉
        zk.setData("/root/childone", "childonemodify".getBytes(), -1);

        //删除/root/childone这个节点，第二个参数为版本，－1的话直接删除，无视版本
        zk.delete("/root/childone", -1);

        //关闭session
        zk.close();
    }

    public static void main(String[] args) throws Exception {
        ZooKeeper zookeeper = new ZooKeeper(HOST, TIME_OUT, null);
        System.out.println("=========创建节点===========");
        if (zookeeper.exists("/test", false) == null) {
            zookeeper.create("/test", "znode1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        System.out.println("=============查看节点是否安装成功===============");
        System.out.println(new String(zookeeper.getData("/test", false, null)));

        System.out.println("=========修改节点的数据==========");
        String data = "zNode2";
        zookeeper.setData("/test", data.getBytes(), -1);

        System.out.println("========查看修改的节点是否成功=========");
        System.out.println(new String(zookeeper.getData("/test", false, null)));

        System.out.println("=======删除节点==========");
        zookeeper.delete("/test", -1);

        System.out.println("==========查看节点是否被删除============");
        System.out.println("节点状态：" + zookeeper.exists("/test", false));

        zookeeper.close();
    }
}

package com.github.sakserv.hbase;

import com.github.sakserv.minicluster.config.ConfigVars;
import com.github.sakserv.minicluster.impl.HbaseLocalCluster;
import com.github.sakserv.minicluster.impl.HdfsLocalCluster;
import com.github.sakserv.minicluster.impl.ZookeeperLocalCluster;
import junit.framework.TestCase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */public class HBaseClientTest {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(HBaseClient.class);

    // Zookeeper Local Cluster config
    private static  Integer zookeeperPort = 21001;
    private static  String zookeeperTempDir = "embedded_zookeeper";
    private static  String zookeeperConnectionString = "localhost:" + zookeeperPort;

    // HDFS Local Cluster config
    private static  Integer hdfsNamenodePort = 21100;
    private static  String hdfsTempDir = "embedded_hdfs";
    private static  Integer hdfsNumDatanodes = 1;
    private static  Boolean hdfsEnablePermissions = false;
    private static  Boolean hdfsFormat = true;

    // HBase Local Cluster config
    private static  Integer hbaseMasterPort = 21200;
    private static  Integer hbaseMasterInfoPort = 21205;
    private static  Integer hbaseNumRegionServers = 1;
    private static  String hbaseRootDirSuffix = "/hbase";
    private static  String hbaseZookeeperZnodeParent = "/hbase";
    private static  Boolean hbaseWalReplicationEnabled = false;

    // Mini clusters
    private static ZookeeperLocalCluster zookeeperLocalCluster;
    private static HdfsLocalCluster hdfsLocalCluster;
    private static HbaseLocalCluster hbaseLocalCluster;

    // Hbase table details
    String tableName = "tableFoo";
    String colFamName = "colFamBar";
    String colQualiferName = "colQualBaz";
    Integer numRowsToPut = 50;

    // Start the zookeeper, hdfs, and hbase local clusters
    @BeforeClass
    public static void setUp() throws Exception {

        // Start Zookeeper
        zookeeperLocalCluster = new ZookeeperLocalCluster.Builder()
                .setPort(zookeeperPort)
                .setTempDir(zookeeperTempDir)
                .setZookeeperConnectionString(zookeeperConnectionString)
                .build();
        zookeeperLocalCluster.start();


        hdfsLocalCluster = new HdfsLocalCluster.Builder()
                .setHdfsNamenodePort(hdfsNamenodePort)
                .setHdfsTempDir(hdfsTempDir)
                .setHdfsNumDatanodes(hdfsNumDatanodes)
                .setHdfsEnablePermissions(hdfsEnablePermissions)
                .setHdfsFormat(hdfsFormat)
                .setHdfsConfig(new Configuration())
                .build();
        hdfsLocalCluster.start();

        hbaseLocalCluster = new HbaseLocalCluster.Builder()
                .setHbaseMasterPort(hbaseMasterPort)
                .setHbaseMasterInfoPort(hbaseMasterInfoPort)
                .setNumRegionServers(hbaseNumRegionServers)
                .setHbaseRootDir("hdfs://localhost:" + hdfsLocalCluster.getHdfsNamenodePort() + hbaseRootDirSuffix)
                .setZookeeperPort(zookeeperPort)
                .setZookeeperConnectionString(zookeeperConnectionString)
                .setZookeeperZnodeParent(hbaseZookeeperZnodeParent)
                .setHbaseWalReplicationEnabled(hbaseWalReplicationEnabled)
                .setHbaseConfiguration(new Configuration())
                .build();
        hbaseLocalCluster.start();

    }

    @AfterClass
    public static void tearDown() throws Exception {
        hbaseLocalCluster.stop();
        hdfsLocalCluster.stop();
        zookeeperLocalCluster.stop();
    }


    @Test
    public void testHbase() throws Exception {

        Configuration configuration = hbaseLocalCluster.getHbaseConfiguration();
        HBaseClient hBaseClient = new HBaseClient();
        hBaseClient.setConfiguration(hbaseLocalCluster.getHbaseConfiguration());

        LOG.info("HBASE: Creating table " + tableName + " with column family " + colFamName);

        hBaseClient.createHbaseTable(tableName, colFamName);

        LOG.info("HBASE: Populate the table with " + numRowsToPut + " rows.");
        for (int i=0; i<numRowsToPut; i++) {
            hBaseClient.putRow(tableName, colFamName, String.valueOf(i), colQualiferName, "row_" + i);
        }

        LOG.info("HBASE: Fetching and comparing the results");
        for (int i=0; i<numRowsToPut; i++) {
            Result result = hBaseClient.getRow(tableName, colFamName, String.valueOf(i), colQualiferName);
            assertEquals("row_" + i, new String(result.value()));
        }

    }
}
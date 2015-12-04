package citexplore.experiment.dataset;

import citexplore.foundation.commandoperability.r.RMath;
import citexplore.foundation.data.io.DataExporter;
import citexplore.foundation.data.io.DataImporter;
import citexplore.foundation.util.Config;
import citexplore.foundation.util.MissingConfigItemException;
import org.apache.commons.io.FileUtils;
import org.apache.mahout.math.SparseMatrix;

import java.io.*;
import java.util.Hashtable;


/**
 * CiteULike数据集处理工具类。
 *
 * @author Xue, Zhuyin; Liu, Xinwei; Xu, Zewen; Zhang, Yin
 */
public class CiteULike {

    // **************** 公开变量

    /**
     * group文件路径。
     */
    public static final String GROUP_PATH = "cx.exp.citeulike.grouppath";

    /**
     * NumberOfUsersInEachGoup文件路径。
     */
    public static final String NUMBER_OF_USERS_IN_ERCH_GOUP_PATH = "cx.exp" +
            ".citeulike.numberofusersineachgouppath";

    /**
     * current文件路径。
     */
    public static final String CURRENT_PATH = "cx.exp.citeulike.currentpath";

    /**
     * UserTagWithTime文件路径前缀。
     */
    public static final String USER_TAG_WITH_TIME_PATH_PREFIX = "cx.exp" +
            ".citeulike" + ".usertagwithtimepathprefix";

    /**
     * WithoutNotagWithTime文件路径。
     */
    public static final String WITHOUT_NOTAG_WITH_TIME_PATH_PREFIX = "cx.exp"
            + "" + ".citeulike.withoutnotagwithtimepathprefix";

    /**
     * LeaveOneUserOneUser文件夹路径，以“/”结尾。
     */
    public static final String LEAVE_ONE_USER_ONE_USER_FOLDER_PREFIX = "cx" +
            ".exp" + "" + ".citeulike.leavaoneuseroneuserfolderprefix";

    // **************** 私有变量

    /**
     * 项目根目录。
     */
    private static String rMathWorkingPath = "";

    /**
     * NumberOfUsersInEachGoup文件路径。
     */
    private static String numberOfUsersInEachGroupPath = "";

    /**
     * current文件路径。
     */
    private static String currentPath = "";

    /**
     * UserLabelWithTime文件路径前缀。
     */
    private static String userTagWithTimePathPrefix = "";

    /**
     * group文件路径。
     */
    private static String groupPath = "";

    /**
     * WithoutNotagWithTime路径。
     */
    private static String withoutNotagWithTimePathPrefix = "";

    /**
     * LeaveOneUserOneUser文件夹路径，以“/”结尾。
     */
    private static String leaveOneUserOneUserFolderPrefix = "";

    // **************** 继承方法

    // **************** 公开方法

    /**
     * 去掉no-tag标签。
     *
     * @param currentPath current文件路径。
     */
    public static void filterNoTag(String currentPath) {
        try {
            int i = 0;
            BufferedReader reader = new BufferedReader(new FileReader(new
                    File(currentPath)));
            BufferedWriter writer = new BufferedWriter(new FileWriter(new
                    File(currentPath + ".filtered_no_tag")));

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                i = i + 1;
                if (i % 1000000 == 0) {
                    System.out.println(i + "lines processed.");
                }

                if (!"no-tag".equals(line.split("\\|")[3])) {
                    writer.write(line);
                    writer.write("\n");
                }
            }

            writer.close();
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 过滤用户资源及标签。
     *
     * @param tru 用户阈值：至少标记过多少资源。
     * @param trr 资源阈值：至少被多少用户标记过。
     * @param trt 标签阈值：至少被多少用户使用过。
     */
    public static void filterUrt(String currentPath, int tru, int trr, int
            trt) {
        try {
            String srcSubfix = ".filter_urt_src";
            String tarSubfix = ".filter_urt_tar";
            FileUtils.copyFile(new File(currentPath), new File(currentPath +
                    srcSubfix));

            boolean stable = false;
            int round = 0;

            while (!stable) {
                round = round + 1;
                stable = true;

                System.out.println("Round " + round);

                // 过滤用户。
                {
                    Hashtable<String, Hashtable<String, Boolean>> urTable =
                            new Hashtable<>();

                    BufferedReader reader = new BufferedReader(new FileReader
                            (new File(currentPath + srcSubfix)));
                    int i = 0;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();

                        i = i + 1;
                        if (i % 1000000 == 0) {
                            System.out.println("Round " + round + " (user): "
                                    + i + " lines read.");
                        }

                        String[] splits = line.split("\\|");
                        String user = splits[1];
                        String resource = splits[0];

                        if (!urTable.containsKey(user)) {
                            urTable.put(user, new Hashtable<String, Boolean>());
                        }

                        if (!urTable.get(user).containsKey(resource)) {
                            urTable.get(user).put(resource, true);
                        }
                    }
                    reader.close();
                    System.out.println("Round " + round + " (user): " +
                            urTable.size() + " users read.");

                    reader = new BufferedReader(new FileReader(new File
                            (currentPath + srcSubfix)));
                    BufferedWriter writer = new BufferedWriter(new FileWriter
                            (new File(currentPath + tarSubfix)));
                    i = 0;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();

                        i = i + 1;
                        if (i % 1000000 == 0) {
                            System.out.println("Round " + round + " (user): "
                                    + i + " lines processed.");
                        }

                        String[] splits = line.split("\\|");
                        String user = splits[1];

                        if (urTable.get(user).size() >= tru) {
                            writer.write(line + "\n");
                        } else {
                            stable = false;
                        }
                    }
                    writer.close();
                    reader.close();

                    new File(currentPath + srcSubfix).delete();
                    new File(currentPath + tarSubfix).renameTo(new File
                            (currentPath + srcSubfix));
                }

                // 过滤资源。
                {
                    Hashtable<String, Hashtable<String, Boolean>> ruTable =
                            new Hashtable<>();

                    BufferedReader reader = new BufferedReader(new FileReader
                            (new File(currentPath + srcSubfix)));
                    int i = 0;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();

                        i = i + 1;
                        if (i % 1000000 == 0) {
                            System.out.println("Round " + round + " " +
                                    "(resource): " + i + " lines read.");
                        }

                        String[] splits = line.split("\\|");
                        String user = splits[1];
                        String resource = splits[0];

                        if (!ruTable.containsKey(resource)) {
                            ruTable.put(resource, new Hashtable<String,
                                    Boolean>());
                        }

                        if (!ruTable.get(resource).containsKey(user)) {
                            ruTable.get(resource).put(user, true);
                        }
                    }
                    reader.close();
                    System.out.println("Round " + round + " (resource): " +
                            ruTable.size() + " resources read.");

                    reader = new BufferedReader(new FileReader(new File
                            (currentPath + srcSubfix)));
                    BufferedWriter writer = new BufferedWriter(new FileWriter
                            (new File(currentPath + tarSubfix)));
                    i = 0;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();

                        i = i + 1;
                        if (i % 1000000 == 0) {
                            System.out.println("Round " + round + " " +
                                    "(resource): " + i + " lines processed.");
                        }

                        String[] splits = line.split("\\|");
                        String resource = splits[0];

                        if (ruTable.get(resource).size() >= trr) {
                            writer.write(line + "\n");
                        } else {
                            stable = false;
                        }
                    }
                    writer.close();
                    reader.close();

                    new File(currentPath + srcSubfix).delete();
                    new File(currentPath + tarSubfix).renameTo(new File
                            (currentPath + srcSubfix));
                }

                // 过滤标签。
                {
                    Hashtable<String, Hashtable<String, Boolean>> tuTable =
                            new Hashtable<>();

                    BufferedReader reader = new BufferedReader(new FileReader
                            (new File(currentPath + srcSubfix)));
                    int i = 0;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();

                        i = i + 1;
                        if (i % 1000000 == 0) {
                            System.out.println("Round " + round + " " +
                                    "(tag): " + i + " lines read.");
                        }

                        String[] splits = line.split("\\|");
                        String user = splits[1];
                        String tag = splits[3];

                        if (!tuTable.containsKey(tag)) {
                            tuTable.put(tag, new Hashtable<String, Boolean>());
                        }

                        if (!tuTable.get(tag).containsKey(user)) {
                            tuTable.get(tag).put(user, true);
                        }
                    }
                    reader.close();
                    System.out.println("Round " + round + " (tag): " +
                            tuTable.size() + " tags read.");

                    reader = new BufferedReader(new FileReader(new File
                            (currentPath + srcSubfix)));
                    BufferedWriter writer = new BufferedWriter(new FileWriter
                            (new File(currentPath + tarSubfix)));
                    i = 0;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();

                        i = i + 1;
                        if (i % 1000000 == 0) {
                            System.out.println("Round " + round + " (tag): "
                                    + i + " lines processed.");
                        }

                        String[] splits = line.split("\\|");
                        String tag = splits[3];

                        if (tuTable.get(tag).size() >= trt) {
                            writer.write(line + "\n");
                        } else {
                            stable = false;
                        }
                    }
                    writer.close();
                    reader.close();

                    new File(currentPath + srcSubfix).delete();
                    new File(currentPath + tarSubfix).renameTo(new File
                            (currentPath + srcSubfix));
                }
            }

            new File(currentPath + srcSubfix).renameTo(new File(currentPath +
                    ".filtered_urt_" + tru + "_" + trr + "_" + trt));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 统计每个group中user数目，生成NumberOfUsersInEachGroup文件。
     * <p/>
     * NumberOfUsersInEachGroup文件格式：sequenceNumber\tgroupID\tuserNumber\n
     */
    public static void numberOfUsersInEachGroup() {
        Hashtable<String, Integer> count = new Hashtable<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new
                    File(getGroupPath())));

            String line;
            while ((line = reader.readLine()) != null) {
                String user = line.trim().split("\\|")[0];

                if (count.containsKey(user)) {
                    count.put(user, count.get(user) + 1);
                } else {
                    count.put(user, 1);
                }
            }

            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(new
                    File(getNumberOfUsersInEachGroupPath())));
            int groupCount = 1;
            for (String groupId : count.keySet()) {
                writer.write(groupCount + "|");
                writer.write(groupId + "|" + count.get(groupId) + "\n");
                groupCount = groupCount + 1;
            }
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 统计指定一组中所有的user资料，生成UserTagWithTime文件。
     * <p/>
     * UserTagWithTime文件格式：userID|documentCountNumber|tag|time\n
     *
     * @param groupId 用户组ID。
     */
    public static void usersInOneGroup(String groupId) {
        Hashtable<String, Boolean> userInGroup = new Hashtable<>();

        try {
            BufferedReader groupReader = new BufferedReader(new FileReader
                    (new File(getGroupPath())));
            String line;
            while ((line = groupReader.readLine()) != null) {
                String[] splits = line.trim().split("\\|");

                if ((groupId.equals(splits[0])) && (!userInGroup.containsKey
                        (splits[1]))) {
                    userInGroup.put(splits[1], true);
                }
            }
            groupReader.close();

            BufferedReader currentReader = new BufferedReader(new FileReader
                    (new File(getCurrentPath())));
            BufferedWriter writer = new BufferedWriter(new FileWriter(new
                    File(getUserTagWithTimePath(groupId))));

            while ((line = currentReader.readLine()) != null) {
                String[] splits = line.trim().split("\\|");

                if (userInGroup.containsKey(splits[1])) {
                    writer.write(splits[1] + "|" + splits[0] + "|" +
                            splits[3] + "|" + splits[2] + "\n");
                }
            }

            writer.close();
            currentReader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取leaveoneuser数据与oneuser数据，存入LeaveOneUserOneUser文件夹路径。
     * <p/>
     * 文件格式：userID|documentCountNumber|tag|time\n
     *
     * @param groupId 用户组ID.
     */
    public static void leaveOneUser(String groupId) {
        Hashtable<String, Boolean> userTable = getUserTable(groupId);

        try {
            String dir = getLeaveOneUserOneUserFolder(groupId);
            new File(dir).mkdir();

            for (String userId : userTable.keySet()) {
                BufferedReader reader = new BufferedReader(new FileReader(new
                        File(getUserTagWithTimePath(groupId))));
                BufferedWriter oneWriter = new BufferedWriter(new FileWriter
                        (new File(dir + "oneUser_" + userId)));
                BufferedWriter leaveOneWriter = new BufferedWriter(new
                        FileWriter(new File(dir + "leaveOneUser_" + userId)));
                int oneLines = 0;

                String line;
                while ((line = reader.readLine()) != null) {
                    if (userId.equals(line.trim().split("\\|")[0])) {
                        oneWriter.write(line.trim() + "\n");
                        oneLines = oneLines + 1;
                    } else {
                        leaveOneWriter.write(line.trim() + "\n");
                    }
                }

                oneWriter.close();
                leaveOneWriter.close();
                reader.close();

                if (oneLines < 4) {
                    new File(dir + "oneUser_" + userId).delete();
                    new File(dir + "leaveOneUser_" + userId).delete();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将oneUser数据分成25%余75%，并分开存入OneUser7525文件夹路径。
     * <p/>
     * 每个标签只保留一行记录。
     * <p/>
     * 文件格式：userID|documentSequenceNumber|tag|time\n
     *
     * @param groupId 用户组ID。
     */
    public static void divide7525(String groupId) {
        Hashtable<String, Boolean> userTable = getUserTable(groupId);

        try {
            for (String userId : userTable.keySet()) {
                if (!oneUserFileExists(groupId, userId)) {
                    continue;
                }

                int lineCount = 0;
                BufferedReader oneUserReader = new BufferedReader(new
                        FileReader(new File(getLeaveOneUserOneUserFolder
                        (groupId) + "oneUser_" + userId)));

                String line;
                while (oneUserReader.readLine() != null) {
                    lineCount++;
                }
                oneUserReader.close();

                String dir = getOneUser7525Folder(groupId);
                new File(dir).mkdir();

                oneUserReader = new BufferedReader(new FileReader(new File
                        (getLeaveOneUserOneUserFolder(groupId) + "oneUser_" +
                        userId)));
                BufferedWriter one75Writer = new BufferedWriter(new
                        FileWriter(new File(dir + "one75_" + userId)));
                BufferedWriter one25Writer = new BufferedWriter(new
                        FileWriter(new File(dir + "one25_" + userId)));
                Hashtable<String, Boolean> one75Table = new Hashtable<>();
                Hashtable<String, Boolean> one25Table = new Hashtable<>();

                int threeQuarterNumber = 3 * lineCount / 4;

                for (int i = 0; (line = oneUserReader.readLine()) != null;
                     i++) {
                    String tag = line.trim().split("\\|")[2];

                    if (i < threeQuarterNumber) {
                        one75Writer.write(line.trim() + "\n");

                        if (!one75Table.containsKey(tag)) {
                            one75Table.put(tag, true);
                        }
                    } else {
                        if (!one75Table.containsKey(tag) && !one25Table
                                .containsKey(tag)) {
                            one25Table.put(tag, true);
                            one25Writer.write(line.trim() + "\n");
                        }
                    }
                }

                one25Writer.close();
                one75Writer.close();
                oneUserReader.close();

                if (one25Table.size() <= 0) {
                    new File(dir + "one25_" + userId).delete();
                    new File(dir + "one75_" + userId).delete();
                    new File(getLeaveOneUserOneUserFolder(groupId) +
                            "oneUser_" + userId).delete();
                    new File(getLeaveOneUserOneUserFolder(groupId) +
                            "leaveOneUser_" + userId).delete();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 根据leaveOneData和one75得到PageRank和personalized PageRank文件。
     * <p/>
     * 每个标签的个性化权重均为1。
     *
     * @param groupId 用户组ID
     */
    public static void leaveOnePRPPR(String groupId) {
        // 循环生成PageRank和personalizedPageRank的结果，并且加上序号重新命名。
        Hashtable<String, Boolean> userTable = getUserTable(groupId);
        int progress = 0;

        try {
            for (String userId : userTable.keySet()) {
                progress = progress + 1;
                System.out.println("User " + progress + " of " + userTable
                        .size() + ":" + userId);

                if (!oneUserFileExists(groupId, userId)) {
                    continue;
                }

                if (new File(getResultOfPrpprFolder(groupId) + "tagTable_" +
                        userId).exists()) {
                    continue;
                }

                // 建立文章及标签表，统计文章标签使用情况。
                Hashtable<String, Integer> paperTable = new Hashtable<>();
                Hashtable<String, Integer> tagTable = new Hashtable<>();
                Hashtable<Integer, Hashtable<Integer, Integer>> matrixTable =
                        new Hashtable<>();
                int pi = 0;
                int ti = 0;
                BufferedReader leaveOneUserReader = new BufferedReader(new
                        FileReader(new File(getLeaveOneUserOneUserFolder
                        (groupId) + "leaveOneUser_" + userId)));
                String line;
                while ((line = leaveOneUserReader.readLine()) != null) {
                    String[] splits = line.trim().split("\\|");
                    if (!paperTable.containsKey(splits[1])) {
                        paperTable.put(splits[1], paperTable.size());
                    }
                    if (!tagTable.containsKey(splits[2])) {
                        tagTable.put(splits[2], tagTable.size());
                    }

                    if (paperTable.containsKey(splits[1])) {
                        pi = paperTable.get(splits[1]);
                    }
                    if (tagTable.containsKey(splits[2])) {
                        ti = tagTable.get(splits[2]);
                    }

                    if (!matrixTable.containsKey(pi)) {
                        matrixTable.put(pi, new Hashtable<Integer, Integer>());
                    }
                    if (!matrixTable.get(pi).containsKey(ti)) {
                        matrixTable.get(pi).put(ti, 0);
                    }
                    matrixTable.get(pi).put(ti, matrixTable.get(pi).get(ti) +
                            1);
                }
                leaveOneUserReader.close();

                int m = paperTable.size();
                int n = tagTable.size();

                // 建立个性化向量。
                SparseMatrix personalizedWeightMatrix = new SparseMatrix(1, n);
                BufferedReader one75Reader = new BufferedReader(new
                        FileReader(new File(getOneUser7525Folder(groupId) +
                        "one75_" + userId)));
                int nonZeros = 0;
                while ((line = one75Reader.readLine()) != null) {
                    String tag = line.trim().split("\\|")[2];
                    if (tagTable.containsKey(tag)) {
                        personalizedWeightMatrix.set(0, tagTable.get(tag), 1);
                        nonZeros = nonZeros + 1;
                    }
                }
                one75Reader.close();

                // 如果个性化向量尺寸为0，则停止执行该用户。
                if (nonZeros == 0) {
                    continue;
                }

                // 将文章标签统计情况转化为稀疏矩阵。
                SparseMatrix paperTagMatrix = new SparseMatrix(m, n);
                SparseMatrix tagPaperMatrix = new SparseMatrix(n, m);
                nonZeros = 0;
                for (int i : matrixTable.keySet()) {
                    for (int j : matrixTable.get(i).keySet()) {
                        paperTagMatrix.set(i, j, matrixTable.get(i).get(j) + 1);
                        tagPaperMatrix.set(j, i, matrixTable.get(i).get(j) + 1);
                        nonZeros = nonZeros + 1;
                    }
                }

                SparseMatrix tagTagMatrix = RMath.matrixMultiply
                        (tagPaperMatrix, nonZeros, paperTagMatrix, nonZeros);

                for (int p = 0; p < n; p++) {
                    tagTagMatrix.set(p, p, 0);
                }

                // 计算行概率
                for (int i = 0; i < n; i++) {
                    double sum = 0.0;

                    for (int j = 0; j < n; j++) {
                        sum = sum + tagTagMatrix.get(i, j);
                    }

                    if (sum != 0.0) {
                        for (int j = 0; j < n; j++) {
                            tagTagMatrix.set(i, j, tagTagMatrix.get(i, j) /
                                    sum);
                        }
                    }
                }

                RMath.pageRank(tagTagMatrix, 0.85, true);

                RMath.personalizedPageRank(tagTagMatrix, 0.85,
                        personalizedWeightMatrix, nonZeros, true);

                new File(getResultOfPrpprFolder(groupId)).mkdir();
                new File(getRMathWorkingPath() + "pagerank.txt").renameTo(new
                        File(getResultOfPrpprFolder(groupId) +
                        "pagerank_" + userId));
                new File(getRMathWorkingPath() + "personalized_pagerank.txt")
                        .renameTo(new File(getResultOfPrpprFolder(groupId) +
                                "personalized_pagerank_" + userId));
                DataExporter.exportStringIntegerTable(getResultOfPrpprFolder
                        (groupId) + "tagTable_" + userId, tagTable, "|");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据leaveOneData和one75得到PageRank和personalized PageRank文件。
     * <p/>
     * 每个标签的个性化权重均为标签出现次数。
     *
     * @param groupId 用户组ID
     */
    public static void leaveOnePRPPR2(String groupId) {
        // 循环生成PageRank和personalizedPageRank的结果，并且加上序号重新命名。
        Hashtable<String, Boolean> userTable = getUserTable(groupId);
        int progress = 0;

        try {
            for (String userId : userTable.keySet()) {
                progress = progress + 1;
                System.out.println("User " + progress + " of " + userTable
                        .size() + ":" + userId);

                if (!oneUserFileExists(groupId, userId)) {
                    continue;
                }

                if (new File(getResultOfPrpprFolder(groupId) + "tagTable_" +
                        userId).exists()) {
                    continue;
                }

                // 建立文章及标签表，统计文章标签使用情况。
                Hashtable<String, Integer> paperTable = new Hashtable<>();
                Hashtable<String, Integer> tagTable = new Hashtable<>();
                Hashtable<Integer, Hashtable<Integer, Integer>> matrixTable =
                        new Hashtable<>();
                int pi = 0;
                int ti = 0;
                BufferedReader leaveOneUserReader = new BufferedReader(new
                        FileReader(new File(getLeaveOneUserOneUserFolder
                        (groupId) + "leaveOneUser_" + userId)));
                String line;
                while ((line = leaveOneUserReader.readLine()) != null) {
                    String[] splits = line.trim().split("\\|");
                    if (!paperTable.containsKey(splits[1])) {
                        paperTable.put(splits[1], paperTable.size());
                    }
                    if (!tagTable.containsKey(splits[2])) {
                        tagTable.put(splits[2], tagTable.size());
                    }

                    if (paperTable.containsKey(splits[1])) {
                        pi = paperTable.get(splits[1]);
                    }
                    if (tagTable.containsKey(splits[2])) {
                        ti = tagTable.get(splits[2]);
                    }

                    if (!matrixTable.containsKey(pi)) {
                        matrixTable.put(pi, new Hashtable<Integer, Integer>());
                    }
                    if (!matrixTable.get(pi).containsKey(ti)) {
                        matrixTable.get(pi).put(ti, 0);
                    }
                    matrixTable.get(pi).put(ti, matrixTable.get(pi).get(ti) +
                            1);
                }
                leaveOneUserReader.close();

                int m = paperTable.size();
                int n = tagTable.size();

                // 建立个性化向量。
                SparseMatrix personalizedWeightMatrix = new SparseMatrix(1, n);
                BufferedReader one75Reader = new BufferedReader(new
                        FileReader(new File(getOneUser7525Folder(groupId) +
                        "one75_" + userId)));
                int nonZeros = 0;
                while ((line = one75Reader.readLine()) != null) {
                    String tag = line.trim().split("\\|")[2];
                    if (tagTable.containsKey(tag)) {
                        if (personalizedWeightMatrix.get(0, tagTable.get(tag)
                        ) == 0.0) {
                            nonZeros = nonZeros + 1;
                        }
                        personalizedWeightMatrix.set(0, tagTable.get(tag),
                                personalizedWeightMatrix.get(0, tagTable.get
                                        (tag)) + 1);
                    }
                }
                one75Reader.close();

                // 如果个性化向量尺寸为0，则停止执行该用户。
                if (nonZeros == 0) {
                    continue;
                }

                // 将文章标签统计情况转化为稀疏矩阵。
                SparseMatrix paperTagMatrix = new SparseMatrix(m, n);
                SparseMatrix tagPaperMatrix = new SparseMatrix(n, m);
                nonZeros = 0;
                for (int i : matrixTable.keySet()) {
                    for (int j : matrixTable.get(i).keySet()) {
                        paperTagMatrix.set(i, j, matrixTable.get(i).get(j) + 1);
                        tagPaperMatrix.set(j, i, matrixTable.get(i).get(j) + 1);
                        nonZeros = nonZeros + 1;
                    }
                }

                SparseMatrix tagTagMatrix = RMath.matrixMultiply
                        (tagPaperMatrix, nonZeros, paperTagMatrix, nonZeros);

                for (int p = 0; p < n; p++) {
                    tagTagMatrix.set(p, p, 0);
                }

                // 计算行概率
                for (int i = 0; i < n; i++) {
                    double sum = 0.0;

                    for (int j = 0; j < n; j++) {
                        sum = sum + tagTagMatrix.get(i, j);
                    }

                    if (sum != 0.0) {
                        for (int j = 0; j < n; j++) {
                            tagTagMatrix.set(i, j, tagTagMatrix.get(i, j) /
                                    sum);
                        }
                    }
                }

                RMath.pageRank(tagTagMatrix, 0.85, true);

                RMath.personalizedPageRank(tagTagMatrix, 0.85,
                        personalizedWeightMatrix, nonZeros, true);

                new File(getResultOfPrpprFolder(groupId)).mkdir();
                new File(getRMathWorkingPath() + "pagerank.txt").renameTo(new
                        File(getResultOfPrpprFolder(groupId) +
                        "pagerank_" + userId));
                new File(getRMathWorkingPath() + "personalized_pagerank.txt")
                        .renameTo(new File(getResultOfPrpprFolder(groupId) +
                                "personalized_pagerank_" + userId));
                DataExporter.exportStringIntegerTable(getResultOfPrpprFolder
                        (groupId) + "tagTable_" + userId, tagTable, "|");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 比较PageRank和PersonalizedPageRank的结果，
     * 若personalizedPageRank>pagerank,则输出对应的tag，写入effectiveLabelsIn25中
     *
     * @param groupId 用户组ID。
     */
    public static void comparePRPPR(String groupId) {
        Hashtable<String, Boolean> userTable = getUserTable(groupId);
        double precisionSum = 0.0;
        double precisionCount = 0;

        try {
            for (String userId : userTable.keySet()) {
                if (!new File(getResultOfPrpprFolder(groupId) +
                        "pagerank_" + userId).exists()) {
                    continue;
                }

                Hashtable<String, Integer> tag25Table = new Hashtable<>();
                Hashtable<Integer, String> tagTable = DataImporter
                        .importIntegerStringTable(getResultOfPrpprFolder
                                (groupId) + "tagTable_" + userId, true, "\\|");
                int tp = 0;
                int no25 = 0;
                int no25nit = 0;
                double result;

                BufferedReader one25Reader = new BufferedReader(new
                        FileReader(new File(getOneUser7525Folder(groupId) +
                        "one25_" + userId)));
                String line;
                while ((line = one25Reader.readLine()) != null) {
                    String tag = line.trim().split("\\|")[2];
                    if (!tag25Table.containsKey(tag)) {
                        tag25Table.put(tag, tag25Table.size());
                        no25++;

                        if (!tagTable.containsValue(tag)) {
                            no25nit++;
                        }
                    }
                }
                one25Reader.close();

                if (no25 == no25nit) {
                    continue;
                }

                //获取pageRank与personalizedPageRank
                double[] pageRank = DataImporter
                        .importDoubleVectorAsDoubleArray(getResultOfPrpprFolder(groupId) +
                                "pagerank_" + userId);
                double[][] personalizedPageRank = DataImporter
                        .importDoubleMatrixAsTwoDimensionalDoubleArray(getResultOfPrpprFolder(groupId) +
                                "personalized_pagerank_" + userId);

                for (int i = 0; i < pageRank.length; i++) {
                    String tag = tagTable.get(i);
                    if (personalizedPageRank[0][i] > pageRank[i] &&
                            tag25Table.containsKey(tag)) {
                        tp++;
                    }
                }

                result = (1.0 * tp) / (no25 - no25nit);
                precisionSum = precisionSum + result;
                precisionCount = precisionCount + 1;

                BufferedWriter precisionWriter = new BufferedWriter(new
                        FileWriter(new File(getLeaveOneUserOneUserFolder
                        (groupId) + "precision_" + userId)));
                precisionWriter.write(Double.toString(result));
                precisionWriter.close();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(new
                    File(getLeaveOneUserOneUserFolder(groupId) +
                    "groupPrecision_" + groupId)));
            writer.write(Double.toString(precisionSum / precisionCount));
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // **************** 私有方法

    /**
     * 获得项目根目录。
     *
     * @return 项目根目录。
     */
    private static String getRMathWorkingPath() {
        if ("".equals(rMathWorkingPath)) {
            if ((rMathWorkingPath = Config.get(RMath.WORKING_FOLDER)) == null) {
                throw new MissingConfigItemException(RMath.WORKING_FOLDER);
            }
        }
        return rMathWorkingPath;
    }

    /**
     * 获得group文件路径。
     *
     * @return group文件路径。
     */
    private static String getGroupPath() {
        if ("".equals(groupPath)) {
            if ((groupPath = Config.get(GROUP_PATH)) == null) {
                throw new MissingConfigItemException(GROUP_PATH);
            }
        }
        return groupPath;
    }

    /**
     * 获得NumberOfUsersInEachGroupPath文件路径。
     *
     * @return NumberOfUsersInEachGroupPath文件路径。
     */
    private static String getNumberOfUsersInEachGroupPath() {
        if ("".equals(numberOfUsersInEachGroupPath)) {
            if ((numberOfUsersInEachGroupPath = Config.get
                    (NUMBER_OF_USERS_IN_ERCH_GOUP_PATH)) == null) {
                throw new MissingConfigItemException
                        (NUMBER_OF_USERS_IN_ERCH_GOUP_PATH);
            }
        }
        return numberOfUsersInEachGroupPath;
    }

    /**
     * 获得currentPath路径。
     *
     * @return currentPath路径。
     */
    private static String getCurrentPath() {
        if ("".equals(currentPath)) {
            if ((currentPath = Config.get(CURRENT_PATH)) == null) {
                throw new MissingConfigItemException(CURRENT_PATH);
            }
        }
        return currentPath;
    }

    /**
     * 获得UserTagWithTimePath文件路径。
     *
     * @param groupId 用户组ID。
     * @return UserTagWithTimePath文件路径
     */
    private static String getUserTagWithTimePath(String groupId) {
        if ("".equals(userTagWithTimePathPrefix)) {
            if ((userTagWithTimePathPrefix = Config.get
                    (USER_TAG_WITH_TIME_PATH_PREFIX)) == null) {
                throw new MissingConfigItemException
                        (USER_TAG_WITH_TIME_PATH_PREFIX);
            }
        }
        return userTagWithTimePathPrefix + "_" + groupId;
    }

    /**
     * 获得LeaveOneUserOneUser文件夹路径，以“/”结尾。
     *
     * @param groupId 用户组ID
     * @return LeaveOneUserOneUser文件夹路径。
     */
    private static String getLeaveOneUserOneUserFolder(String groupId) {
        if ("".equals(leaveOneUserOneUserFolderPrefix)) {
            if ((leaveOneUserOneUserFolderPrefix = Config.get
                    (LEAVE_ONE_USER_ONE_USER_FOLDER_PREFIX)) == null) {
                throw new MissingConfigItemException
                        (LEAVE_ONE_USER_ONE_USER_FOLDER_PREFIX);
            }
        }
        return leaveOneUserOneUserFolderPrefix + "_" + groupId + "/";
    }

    /**
     * 获得OneUser7525Folder文件路径,，以“/”结尾。
     *
     * @param groupId 用户组ID
     * @return OneUser7525Folder文件路径。
     */
    private static String getOneUser7525Folder(String groupId) {
        return getLeaveOneUserOneUserFolder(groupId) + "7525/";
    }

    /**
     * 获得ResultOfRrppr文件夹路径，以“/”结尾。
     *
     * @param groupId 用户组ID
     * @return ResultOfRrppr文件夹方法。
     */
    private static String getResultOfPrpprFolder(String groupId) {
        return getLeaveOneUserOneUserFolder(groupId) + "resultOfPrppr/";
    }

    /**
     * 用于返回用户Table。
     *
     * @param groupId 用户组ID。
     * @return 用户Table。
     */
    private static Hashtable<String, Boolean> getUserTable(String groupId) {
        Hashtable<String, Boolean> userTable = new Hashtable<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new
                    File(getUserTagWithTimePath(groupId))));
            String line;
            while ((line = reader.readLine()) != null) {
                String user = line.trim().split("\\|")[0];
                if (!userTable.containsKey(user)) {
                    userTable.put(user, true);
                }
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userTable;
    }

    /**
     * 检查oneUser文件是否存在。
     *
     * @param groupId 用户组ID。
     * @param userId  用户ID。
     * @return oneUser文件是否存在。
     */
    private static boolean oneUserFileExists(String groupId, String userId) {
        return new File(getLeaveOneUserOneUserFolder(groupId) + "oneUser_" +
                userId).exists();
    }
}

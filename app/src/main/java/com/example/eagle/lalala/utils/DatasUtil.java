package com.example.eagle.lalala.utils;


import com.example.eagle.lalala.PDM.FriendPDM;
import com.example.eagle.lalala.PDM.MarksPDM;
import com.example.eagle.lalala.bean.CircleItem;
import com.example.eagle.lalala.bean.CommentItem;
import com.example.eagle.lalala.bean.FavortItem;
import com.example.eagle.lalala.bean.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author yiw
 * @ClassName: DatasUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015-12-28 下午4:16:21
 */
public class DatasUtil {

    public static String mCurrentFragment = "map_frag";
    public static final String[] CONTENTS = {"", "哈哈", "今天是个好日子", "呵呵", "图不错",
            "我勒个去"};
    public static final String[] PHOTOS = {
            "http://f.hiphotos.baidu.com/image/pic/item/faf2b2119313b07e97f760d908d7912396dd8c9c.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/4b90f603738da977c76ab6fab451f8198718e39e.jpg",
            "http://e.hiphotos.baidu.com/image/pic/item/902397dda144ad343de8b756d4a20cf430ad858f.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/a6efce1b9d16fdfa0fbc1ebfb68f8c5495ee7b8b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/a71ea8d3fd1f4134e61e0f90211f95cad1c85e36.jpg",
            "http://c.hiphotos.baidu.com/image/pic/item/7dd98d1001e939011b9c86d07fec54e737d19645.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/f11f3a292df5e0fecc3e83ef586034a85edf723d.jpg",
            "http://cdn.duitang.com/uploads/item/201309/17/20130917111400_CNmTr.thumb.224_0.png",
            "http://pica.nipic.com/2007-10-17/20071017111345564_2.jpg",
            "http://pic4.nipic.com/20091101/3672704_160309066949_2.jpg",
            "http://pic4.nipic.com/20091203/1295091_123813163959_2.jpg",
            "http://pic31.nipic.com/20130624/8821914_104949466000_2.jpg",
            "http://pic6.nipic.com/20100330/4592428_113348099353_2.jpg",
            "http://pic9.nipic.com/20100917/5653289_174356436608_2.jpg",
            "http://img10.3lian.com/sc6/show02/38/65/386515.jpg",
            "http://pic1.nipic.com/2008-12-09/200812910493588_2.jpg",
            "http://pic2.ooopic.com/11/79/98/31bOOOPICb1_1024.jpg"};
    public static final String[] HEADIMG = {
            "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1208/20/c3/13065102_1345473341142_800x800.jpg",
            "http://tse4.mm.bing.net/th?id=OIP.Me8bcba93c4c513e76cc8b0217d8546a5o0&w=235&h=145&c=7&rs=1&qlt=90&o=4&pid=1.1",
            "http://tse1.mm.bing.net/th?&id=OIP.Mb11d8a6de029a42d346ed4ad9120e7d6o0&w=300&h=187&c=0&pid=1.9&rs=0&p=0",
            "http://tse1.mm.bing.net/th?id=OIP.Mb153dfd34a7bf3de39bac9f421be3d1co0&w=233&h=144&c=7&rs=1&qlt=90&o=4&pid=1.1",
            "http://tse1.mm.bing.net/th?&id=OIP.Me3fd4652147671728223a428d2cddabao0&w=300&h=225&c=0&pid=1.9&rs=0&p=0",
            "http://tse2.mm.bing.net/th?id=OIP.Maf3d0b615534fc34707a482f2fb80daao0&w=219&h=123&c=7&rs=1&qlt=90&o=4&pid=1.1",};

    public static List<CircleItem> circleDatas;

    public static List<MarksPDM> sMarksPDMs_public = new ArrayList<MarksPDM>();
    public static List<FriendPDM> sFriendsPDMs = new ArrayList<FriendPDM>();

    public static List<User> users = new ArrayList<User>();
    /**
     * 动态id自增长
     */
    private static int circleId = 0;
    /**
     * 点赞id自增长
     */
    private static int favortId = 0;
    /**
     * 评论id自增长
     */
    private static int commentId = 0;
    public static final User curUser = new User("0", "道不明", HEADIMG[0]);

    static {
        User user1 = new User("1", "老邓", HEADIMG[1]);
        User user2 = new User("2", "Guan_Suns", HEADIMG[2]);
        User user3 = new User("3", "Katherine", HEADIMG[3]);
        User user4 = new User("4", "hy", HEADIMG[4]);
        User user5 = new User("5", "Fool", HEADIMG[1]);
        User user6 = new User("6", "Stupid", HEADIMG[2]);
        User user7 = new User("7", "孙", HEADIMG[3]);
        User user8 = new User("8", "李", HEADIMG[4]);

        users.add(curUser);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
//        users.add(user7);
//        users.add(user8);

        mCurrentFragment = "map_frag";
    }

    public static List<CircleItem> getCircleDatas() {
        return circleDatas;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static List<CircleItem> createCircleDatas() {
        circleDatas = new ArrayList<CircleItem>();
        for (int i = 0; i < 15; i++) {
            CircleItem item = new CircleItem();
            User user = getUser();
            item.setId(String.valueOf(circleId++));
            item.setUser(user);
            item.setContent(getContent());
            item.setCreateTime("2016/4/20");

            item.setFavorters(createFavortItemList());
            item.setComments(createCommentItemList());
            item.setPhoto(createPhoto());

            circleDatas.add(item);
        }

        return circleDatas;
    }

    public static User getUser() {
        return users.get(getRandomNum(users.size()));
    }

    public static String getContent() {
        return CONTENTS[getRandomNum(CONTENTS.length)];
    }

    public static int getRandomNum(int max) {
        Random random = new Random();
        int result = random.nextInt(max);
        return result;
    }

    public static String createPhoto() {
        return PHOTOS[getRandomNum(PHOTOS.length)];
    }

    public static List<FavortItem> createFavortItemList() {
        int size = getRandomNum(users.size());
        List<FavortItem> items = new ArrayList<FavortItem>();
        List<String> history = new ArrayList<String>();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                FavortItem newItem = createFavortItem();
                String userid = newItem.getUser().getId();
                if (!history.contains(userid)) {
                    items.add(newItem);
                    history.add(userid);
                } else {
                    i--;
                }
            }
        }
        return items;
    }

    public static FavortItem createFavortItem() {
        FavortItem item = new FavortItem();
        item.setId(String.valueOf(favortId++));
        item.setUser(getUser());
        return item;
    }

    public static FavortItem createCurUserFavortItem() {
        FavortItem item = new FavortItem();
        item.setId(String.valueOf(favortId++));
        item.setUser(curUser);
        return item;
    }

    public static List<CommentItem> createCommentItemList() {
        List<CommentItem> items = new ArrayList<CommentItem>();
        int size = getRandomNum(10);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                items.add(createComment());
            }
        }
        return items;
    }

    public static CommentItem createComment() {
        CommentItem item = new CommentItem();
        item.setId(String.valueOf(commentId++));
        item.setContent("哈哈");
        User user = getUser();
        item.setUser(user);
//        if (getRandomNum(10) % 2 == 0) {
//            while (true) {
//                User replyUser = getUser();
//                if (!user.getId().equals(replyUser.getId())) {
//                    item.setToReplyUser(replyUser);
//                    break;
//                }
//            }
//        }
        return item;
    }

    /**
     * 创建发布评论
     *
     * @return
     */
    public static CommentItem createPublicComment(String content) {
        CommentItem item = new CommentItem();
        item.setId(String.valueOf(commentId++));
        item.setContent(content);
        item.setUser(curUser);
        return item;
    }

    /**
     * 创建回复评论
     *
     * @return
     */
    public static CommentItem createReplyComment(User replyUser, String content) {
        CommentItem item = new CommentItem();
        item.setId(String.valueOf(commentId++));
        item.setContent(content);
        item.setUser(curUser);
        item.setToReplyUser(replyUser);
        return item;
    }
}

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
            "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s160-c/A%252520Photographer.jpg",
            "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s160-c/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg",
            "https://lh5.googleusercontent.com/-7qZeDtRKFKc/URquWZT1gOI/AAAAAAAAAbs/hqWgteyNXsg/s160-c/Another%252520Rockaway%252520Sunset.jpg",
            "https://lh3.googleusercontent.com/--L0Km39l5J8/URquXHGcdNI/AAAAAAAAAbs/3ZrSJNrSomQ/s160-c/Antelope%252520Butte.jpg",
            "https://lh6.googleusercontent.com/-8HO-4vIFnlw/URquZnsFgtI/AAAAAAAAAbs/WT8jViTF7vw/s160-c/Antelope%252520Hallway.jpg",
            "https://lh4.googleusercontent.com/-WIuWgVcU3Qw/URqubRVcj4I/AAAAAAAAAbs/YvbwgGjwdIQ/s160-c/Antelope%252520Walls.jpg",
            "https://lh6.googleusercontent.com/-UBmLbPELvoQ/URqucCdv0kI/AAAAAAAAAbs/IdNhr2VQoQs/s160-c/Apre%2525CC%252580s%252520la%252520Pluie.jpg",
            "https://lh3.googleusercontent.com/-s-AFpvgSeew/URquc6dF-JI/AAAAAAAAAbs/Mt3xNGRUd68/s160-c/Backlit%252520Cloud.jpg",
            "https://lh5.googleusercontent.com/-bvmif9a9YOQ/URquea3heHI/AAAAAAAAAbs/rcr6wyeQtAo/s160-c/Bee%252520and%252520Flower.jpg",
            "https://lh5.googleusercontent.com/-n7mdm7I7FGs/URqueT_BT-I/AAAAAAAAAbs/9MYmXlmpSAo/s160-c/Bonzai%252520Rock%252520Sunset.jpg",
            "https://lh6.googleusercontent.com/-4CN4X4t0M1k/URqufPozWzI/AAAAAAAAAbs/8wK41lg1KPs/s160-c/Caterpillar.jpg",
            "https://lh3.googleusercontent.com/-rrFnVC8xQEg/URqufdrLBaI/AAAAAAAAAbs/s69WYy_fl1E/s160-c/Chess.jpg",
            "https://lh5.googleusercontent.com/-WVpRptWH8Yw/URqugh-QmDI/AAAAAAAAAbs/E-MgBgtlUWU/s160-c/Chihuly.jpg",
            "https://lh5.googleusercontent.com/-0BDXkYmckbo/URquhKFW84I/AAAAAAAAAbs/ogQtHCTk2JQ/s160-c/Closed%252520Door.jpg",
            "https://lh3.googleusercontent.com/-PyggXXZRykM/URquh-kVvoI/AAAAAAAAAbs/hFtDwhtrHHQ/s160-c/Colorado%252520River%252520Sunset.jpg",
            "https://lh3.googleusercontent.com/-ZAs4dNZtALc/URquikvOCWI/AAAAAAAAAbs/DXz4h3dll1Y/s160-c/Colors%252520of%252520Autumn.jpg",
            "https://lh4.googleusercontent.com/-GztnWEIiMz8/URqukVCU7bI/AAAAAAAAAbs/jo2Hjv6MZ6M/s160-c/Countryside.jpg",
            "https://lh4.googleusercontent.com/-bEg9EZ9QoiM/URquklz3FGI/AAAAAAAAAbs/UUuv8Ac2BaE/s160-c/Death%252520Valley%252520-%252520Dunes.jpg",
            "https://lh6.googleusercontent.com/-ijQJ8W68tEE/URqulGkvFEI/AAAAAAAAAbs/zPXvIwi_rFw/s160-c/Delicate%252520Arch.jpg",
            "https://lh5.googleusercontent.com/-Oh8mMy2ieng/URqullDwehI/AAAAAAAAAbs/TbdeEfsaIZY/s160-c/Despair.jpg",
            "https://lh5.googleusercontent.com/-gl0y4UiAOlk/URqumC_KjBI/AAAAAAAAAbs/PM1eT7dn4oo/s160-c/Eagle%252520Fall%252520Sunrise.jpg",
            "https://lh3.googleusercontent.com/-hYYHd2_vXPQ/URqumtJa9eI/AAAAAAAAAbs/wAalXVkbSh0/s160-c/Electric%252520Storm.jpg",
            "https://lh5.googleusercontent.com/-PyY_yiyjPTo/URqunUOhHFI/AAAAAAAAAbs/azZoULNuJXc/s160-c/False%252520Kiva.jpg",
            "https://lh6.googleusercontent.com/-PYvLVdvXywk/URqunwd8hfI/AAAAAAAAAbs/qiMwgkFvf6I/s160-c/Fitzgerald%252520Streaks.jpg",
            "https://lh4.googleusercontent.com/-KIR_UobIIqY/URquoCZ9SlI/AAAAAAAAAbs/Y4d4q8sXu4c/s160-c/Foggy%252520Sunset.jpg",
            "https://lh6.googleusercontent.com/-9lzOk_OWZH0/URquoo4xYoI/AAAAAAAAAbs/AwgzHtNVCwU/s160-c/Frantic.jpg",
            "https://lh3.googleusercontent.com/-0X3JNaKaz48/URqupH78wpI/AAAAAAAAAbs/lHXxu_zbH8s/s160-c/Golden%252520Gate%252520Afternoon.jpg",
            "https://lh6.googleusercontent.com/-95sb5ag7ABc/URqupl95RDI/AAAAAAAAAbs/g73R20iVTRA/s160-c/Golden%252520Gate%252520Fog.jpg",
            "https://lh3.googleusercontent.com/-JB9v6rtgHhk/URqup21F-zI/AAAAAAAAAbs/64Fb8qMZWXk/s160-c/Golden%252520Grass.jpg",
            "https://lh4.googleusercontent.com/-EIBGfnuLtII/URquqVHwaRI/AAAAAAAAAbs/FA4McV2u8VE/s160-c/Grand%252520Teton.jpg",
            "https://lh4.googleusercontent.com/-WoMxZvmN9nY/URquq1v2AoI/AAAAAAAAAbs/grj5uMhL6NA/s160-c/Grass%252520Closeup.jpg",
            "https://lh3.googleusercontent.com/-6hZiEHXx64Q/URqurxvNdqI/AAAAAAAAAbs/kWMXM3o5OVI/s160-c/Green%252520Grass.jpg",
            "https://lh5.googleusercontent.com/-6LVb9OXtQ60/URquteBFuKI/AAAAAAAAAbs/4F4kRgecwFs/s160-c/Hanging%252520Leaf.jpg",
            "https://lh4.googleusercontent.com/-zAvf__52ONk/URqutT_IuxI/AAAAAAAAAbs/D_bcuc0thoU/s160-c/Highway%2525201.jpg"};
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

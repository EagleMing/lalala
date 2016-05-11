package com.example.eagle.lalala.mvp.presenter;

import android.view.View;

import com.example.eagle.lalala.Activity.MainActivity;
import com.example.eagle.lalala.PDM.commentsPDM;
import com.example.eagle.lalala.PDM.likesPDM;
import com.example.eagle.lalala.bean.CommentConfig;
import com.example.eagle.lalala.bean.CommentItem;
import com.example.eagle.lalala.bean.FavortItem;
import com.example.eagle.lalala.mvp.modle.CircleModel;
import com.example.eagle.lalala.mvp.modle.IDataRequestListener;
import com.example.eagle.lalala.mvp.view.ICircleView;
import com.example.eagle.lalala.utils.DatasUtil;

import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;


/**
 * @author yiw
 * @ClassName: CirclePresenter
 * @Description: 通知model请求服务器和通知view更新
 * @date 2015-12-28 下午4:06:03
 */
public class CirclePresenter {
    private CircleModel mCircleModel;
    private ICircleView mCircleView;

    public CirclePresenter(ICircleView view) {
        this.mCircleView = view;
        mCircleModel = new CircleModel();
    }

    /**
     * @param circleId
     * @return void    返回类型
     * @throws
     * @Title: deleteCircle
     * @Description: 删除动态
     */
    public void deleteCircle(final long circleId) {
        mCircleModel.deleteCircle(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                mCircleView.update2DeleteCircle(circleId);
            }
        });
    }

    /**
     * @param circlePosition
     * @return void    返回类型
     * @throws
     * @Title: addFavort
     * @Description: 点赞
     */
    public void addFavort(final int circlePosition) {
        mCircleModel.addFavort(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                likesPDM temp = new likesPDM(6545481, MainActivity.userId,DatasUtil.sMarksPDMs_public.get(circlePosition).getMarkId() ,DatasUtil.sMarksPDMs_public.get(circlePosition).getUserName());
                mCircleView.update2AddFavorite(circlePosition, temp);
            }
        });
    }

    /**
     * @param @param circlePosition
     * @param @param favortId
     * @return void    返回类型
     * @throws
     * @Title: deleteFavort
     * @Description: 取消点赞
     */
    public void deleteFavort(final int circlePosition, final long favortId) {
        mCircleModel.deleteFavort(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                mCircleView.update2DeleteFavort(circlePosition, favortId);
            }
        });
    }

    /**
     * @param content
     * @param config  CommentConfig
     * @return void    返回类型
     * @throws
     * @Title: addComment
     * @Description: 增加评论
     */
    public void addComment(final String content, final CommentConfig config) {
        if (config == null) {
            return;
        }
        mCircleModel.addComment(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                commentsPDM newItem = new commentsPDM(DatasUtil.sMarksPDMs_public.get(config.circlePosition).getMarkId(),
                        MainActivity.userId,MainActivity.userName,content,new Timestamp(System.currentTimeMillis()));
//                public commentsPDM(long markId, long friendId, String friendName, String content, Timestamp commentTime)
//                if (config.commentType == CommentConfig.Type.PUBLIC) {
//                    newItem = DatasUtil.createPublicComment(content);
//                } else if (config.commentType == CommentConfig.Type.REPLY) {
//                    newItem = DatasUtil.createReplyComment(config.replyUser, content);
//                }
                mCircleView.update2AddComment(config.circlePosition, newItem);
            }

        });
    }

    /**
     * @param @param circlePosition
     * @param @param commentId
     * @return void    返回类型
     * @throws
     * @Title: deleteComment
     * @Description: 删除评论
     */
    public void deleteComment(final int circlePosition, final long commentId) {
        mCircleModel.deleteComment(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                mCircleView.update2DeleteComment(circlePosition, commentId);
            }

        });
    }

    /**
     * @param commentConfig
     */
    public void showEditTextBody(CommentConfig commentConfig) {
        mCircleView.updateEditTextBodyVisible(View.VISIBLE, commentConfig);
    }

}

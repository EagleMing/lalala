package com.example.eagle.lalala.utils;



import com.example.eagle.lalala.PDM.FriendPDM;
import com.example.eagle.lalala.bean.User;

import java.util.Comparator;

public class PinyinComparator implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		// 按照名字排序
		FriendPDM user0 = (FriendPDM) arg0;
		FriendPDM user1 = (FriendPDM) arg1;
		char catalog0 = ' ';
		char catalog1 = ' ';

		if (user0 != null && user0.getUserName() != null
				&& user0.getUserName().length() > 1) {
			catalog0 = PingYinUtil.converterToFirstSpell(user0.getUserName())
					.charAt(0);
			if(catalog0 >= 'a'&&catalog0 <= 'z')
				catalog0 -= 32;
		}

		if (user1 != null && user1.getUserName() != null
				&& user1.getUserName().length() > 1)
			catalog1 = PingYinUtil.converterToFirstSpell(user1.getUserName())
					.charAt(0);
		if(catalog1 >= 'a'&&catalog1 <= 'z')
			catalog1 -= 32;
		//int flag = catalog0.compareTo(catalog1);

		return catalog0 - catalog1;

	}

}

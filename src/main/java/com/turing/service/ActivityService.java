package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.QuestionAndAnswer;
import com.turing.entity.User;
import com.turing.entity.dto.ActivityDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月29日 13:43:52
 */
public interface ActivityService {

    Result addActivity (User user, ActivityDto activityDto, QuestionAndAnswer[] questionAndAnswers);

    Result operateActivityCover (MultipartFile file, Long id);

    Result getActivity ();

    Result updateActivity (User user, ActivityDto activityDto, QuestionAndAnswer[] questionAndAnswers);

    Result passExamine (Long id);

    Result withdraw (Long id);

    Result getActivityById (Long id);
}

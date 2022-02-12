package com.turing.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.turing.common.ActivityStatus;
import com.turing.entity.Activity;
import com.turing.entity.QuestionAndAnswer;
import com.turing.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.*;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月29日 13:08:15
 */
@Data
@ApiModel(value = "ActivityDto", description = "活动信息")
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDto implements Serializable {

    private static final long serialVersionUID = -8485836040577460714L;
    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty(required = true)
    private String content;

    @ApiModelProperty(hidden = true)
    private String status;

    @ApiModelProperty(hidden = true)
    private String cover;

    @ApiModelProperty(required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty(required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date deadline;

    @ApiModelProperty(hidden = true, required = false)
    private UserDto userDto;
    @ApiModelProperty(required = true)
    private List<String> tags;
    @ApiModelProperty(hidden = true, required = false)
    private Map<String, String> QAInfo;

    public void transform (Activity activity, List<QuestionAndAnswer> QAList, User user) {
        init();
        BeanUtils.copyProperties(activity, this);

        switch (activity.getStatus()) {
            case -1:
                this.setStatus(ActivityStatus.INVALID.getStatus());
                break;
            case 0:
                this.setStatus(ActivityStatus.EXAMINE.getStatus());
                break;
            case 1:
                this.setStatus(ActivityStatus.EFFECTIVE.getStatus());
                break;
        }
        this.setTags(Arrays.asList(activity.getTags().split(",")));
        for (QuestionAndAnswer questionAndAnswer : QAList) {
            this.QAInfo.put(questionAndAnswer.getQuestion(), questionAndAnswer.getAnswer());
        }
        userDto.transform(user);
    }

    private void init () {
        if (userDto == null) {
            this.userDto = new UserDto();
        }
        if (QAInfo == null) {
            this.QAInfo = new HashMap<>();
        }
    }

}

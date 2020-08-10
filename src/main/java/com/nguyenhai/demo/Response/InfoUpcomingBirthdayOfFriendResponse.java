package com.nguyenhai.demo.Response;

import com.nguyenhai.demo.Entity.InfoUser;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoUpcomingBirthdayOfFriendResponse {

    private List<OneInfo> today;

    private HashMap<String, List<OneInfo>> inMonths;

    public static InfoUpcomingBirthdayOfFriendResponse getInstance() {
        return new InfoUpcomingBirthdayOfFriendResponse(new ArrayList<>(), new HashMap<>());
    }

    @Getter
    @Setter
    private static class OneInfo extends BasicUserInfoResponse {
        private Date birthDay;
        public OneInfo(InfoUser infoUser) {
            super();
            setId(infoUser.getId());
            setFirstName(infoUser.getFirstName());
            setLastName(infoUser.getLastName());
            setUrlAvatar(infoUser.getUrlAvatar());
            this.birthDay = infoUser.getBirthDay();
        }
    }

    public void addToday(InfoUser infoUser) {
        this.today.add(new OneInfo(infoUser));
    }

    public void addMonth(String month, InfoUser infoUser) {
        if (!this.inMonths.containsKey(month)) {
            this.inMonths.put(month, new ArrayList<>());
        }

        this.inMonths.get(month).add(new OneInfo(infoUser));
    }

}

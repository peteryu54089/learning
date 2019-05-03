package dao;

import model.Announcement;

import java.util.List;

public interface AnnouncementDao {
    static final int ITEM_PER_PAGE = 10;

    List<Announcement> listAvailableAnnouncements();

    int getItemCount();

    List<Announcement> getAnnouncements(int page);

    int insertAnnouncement(Announcement announcement);

    int updateAnnouncement(Announcement announcement);

    boolean deleteAnnouncement(int id);
}

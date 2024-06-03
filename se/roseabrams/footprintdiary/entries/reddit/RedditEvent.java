package se.roseabrams.footprintdiary.entries.reddit;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

public abstract class RedditEvent extends DiaryEntry {
    /*
     * posts: id,permalink,date,ip,subreddit,gildings,title,url,body
     * post_headers: id,permalink,date,ip,subreddit,gildings,url
     * comments: id,permalink,date,ip,subreddit,gildings,link,parent,body,media
     * comment_headers: id,permalink,date,ip,subreddit,gildings,link,parent
     * post_votes: id,permalink,direction
     * comment_votes: id,permalink,direction
     * messages: id,permalink,thread_id,date,ip,from,to,subject,body
     * message_headers: id,permalink,thread_id,date,ip,from,to
     * 
     * single quotes for cell, double quotes for quotes within string
     */
    public final String ID;

    public RedditEvent(DiaryDate dd, String id) {
        super(DiaryEntryCategory.REDDIT, dd);
        ID = id.intern();
    }
}

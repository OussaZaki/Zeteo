package app.meditel.zeteo;


import java.util.Date;
import java.util.LinkedList;

/**
 * Created by FOla Yinka on 1/3/14.
 */


/* Database
    TABLE COMMENTS ( UNIQUE_ID VARCHAR UNIQUE PRIMARY KEY, COMMENT LONG_VARCHAR )
    TABLE USERS ( UNIQUE_ID VARCHAR UNIQUE PRIMARY KEY, USER_NAME VARCHAR, VOTE_UPS INTEGER,
                  VOTE_DOWNS INTEGER, REP_POINTS INTEGER, COMMENTS LONG_VARCHAR, ANSWERS LONG_VARCHAR,
                  QUESTIONS LONG_VARCHAR )
    TABLE QUESTIONS ( UNIQUE_ID VARCHAR UNIQUE PRIMARY KEY, POST LONG_VARCHAR, USER VARCHAR,
                      VOTE_UPS INTEGER, VOTE_DOWNS INTEGER,  TIME DATE )
    TABLE ANSWERS ( UNIQUE_ID VARCHAR UNIQUE PRIMARY KEY, POST LONG_VARCHAR, USER VARCHAR,
                    VOTE_UPS INTEGER, VOTE_DOWNS INTEGER, COMMENTS LONG_VARCHAR, TIME DATE, COMMENTS LONG_VARCHAR )
 */
public class Classes {

    class User {
        String uniqueID = "";
        String userName = "";
        Integer voteUps = 0;
        Integer voteDowns = 0;
        LinkedList<Answer> answers = new LinkedList<Answer>();
        LinkedList<Comment> comments = new LinkedList<Comment>();
        LinkedList<Question> questions = new LinkedList<Question>();
        Integer repPoints = 0;
    }


    class Comment {
        String comment;
        Integer likes = 0;
        Date time;
        String UniqueID = "";
    }

    class Post {
        User poster = new User();
        String post = "";
        Integer voteUps = 0;
        Integer voteDowns = 0;
        LinkedList<Comment> comments = new LinkedList<Comment>();
        Date time;
    }

    class Answer extends Post {
        Question question;
    }

    class Question extends Post {
        LinkedList<Answer> answers = new LinkedList<Answer>();
    }
}

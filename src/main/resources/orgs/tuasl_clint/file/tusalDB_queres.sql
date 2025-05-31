SELECT * FROM users  ;
SELECT * FROM  user_settings ;
SELECT * FROM  sessions ;
SELECT * FROM  messages ;
SELECT * FROM   media;
SELECT * FROM  contacts ;
SELECT * FROM  chats ;
SELECT * FROM  chat_participants ;
SELECT * FROM  blocked_users ;

-- القنوات حق المستخدم 
SELECT chats.* FROM users LEFT JOIN chat_participants on users.id = chat_participants.user_id LEFT JOIN chats on chat_participants.chat_id = chats.id WHERE users.id = 2;
-- الرسائل حق الشات 
SELECT messages.* ,media.* FROM chats left join messages on chats.id = messages.chat_id LEFT JOIN media on messages.media_id =   media.id  WHERE chats.id = 4  ORDER BY messages.id  ASC; 
SELECT messages.* ,media.* FROM chats left join messages on chats.id = messages.chat_id LEFT JOIN media on messages.media_id =   media.id  WHERE chats.chat_name = "احمد"  ORDER BY messages.id  ASC; 



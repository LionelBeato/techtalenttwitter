MERGE INTO `role` VALUES (1,'USER');

MERGE INTO `user` VALUES (
1,
1,
CURRENT_TIME,
'email@mail.com',
'first-name',
'last-name',
'$2a$12$oMfQx55MFj0C9eX5wST3CeqYHXf7wJHE0aUjsac3g0GN6GhVTI/GC',
'user');

MERGE INTO `user_role` VALUES (
1,
1
)
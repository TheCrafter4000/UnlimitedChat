UnlimitedChat
===================

A mod for Minecraft 1.7.10

# About
This mod removes the annoying character limitation vanilla minecraft has. It uses ForgeEssentials to set a specific character limit each player cannot exceed. Another feature is the option to send client-side commands to the server. This can be useful in case of an older hackclient without gui, of if you just want to know what people are doing.

# Permissions 
The permission value for a player's specific limit is _cu.charlimit_. E.g. `/perm groups _OPS_ value cu.charlimit 200` sets this limit to 200 for all OPs.  
The permission _cu.ignorespam_ disables the built-in spam filter. E.g. `/perm groups _OPS_ allow cu.ignorespam`. Recommended for staff only.  
_cu.clientcommands.send_ and _cu.clientcommands.read_ are handled the same way. The first indicates that a client should send client commands, and the last if a player can see them printed in chat. 

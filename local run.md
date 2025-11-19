docker run \
--name gen \
--restart=always \
-p 6969:6969 \
-v ~/Documents/git/gen:/opt/gen/ \
-v ~/Documents/git/gen/conf/:/gen/conf/ \
-v ~/Documents/git/gen/ext:/gen/ext \
-d tanghc2020/gen:latest




docker run \
--name gen \
--restart=always \
-p 6969:6969 \
-v $HOME/gen:/opt/gen \
-v $HOME/gen/conf:/gen/conf \
-v $HOME/gen/ext:/gen/ext \
-d tanghc2020/gen:latest

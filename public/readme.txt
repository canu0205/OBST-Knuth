# To run all the test cases in batch, enter the following command at the prompt.
#	% sh readme.txt

JAVA_OPTS="-Xms1024m -Xmx4096m"

java MainBst public/10words.txt public/10words.txt > 10words.out

java MainBst public/1000words.txt public/2000words.txt > 1000words.out

java MainBst public/1000words.txt public/2000words2.txt > 1000words2.out

java ${JAVA_OPTS} MainBst public/sawyer.txt public/sawyer.txt > sawyer.out

java ${JAVA_OPTS} MainBst public/sawyer.txt public/mohicans.txt > sawyer-mohicans.out

java ${JAVA_OPTS} MainBst public/mohicans.txt public/mohicans.txt > mohicans.out

java ${JAVA_OPTS} MainBst public/mohicans.txt public/sawyer.txt > mohicans-sawyer.out


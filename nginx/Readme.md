apt install nginx on a vm
add in /etc/hosts the ip of the nginx proxy (here i used the same machine):
127.0.0.1 pfe.localhost  
we paste nginx.conf in /etc/nginx/nginx.conf:
###########
server 192.168.1.3:8080;
server 192.168.1.4:8080;
etc ... (these are cluster nodes)
###########
sudo service nginx restart
and hit pfe.localahost in the browser

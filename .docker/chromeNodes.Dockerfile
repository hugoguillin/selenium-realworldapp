FROM selenium/node-chrome:4.21.0-20240522

# install ca-certificates and libnss3-tools for certutils
RUN sudo apt-get update && sudo apt-get install -y ca-certificates libnss3-tools

# add your certificate to the system trust store
ADD localhost.crt /usr/local/share/ca-certificates/localhost.crt
RUN sudo chmod 644 /usr/local/share/ca-certificates/localhost.crt && sudo update-ca-certificates

# add your CA certificate to the NSS database
RUN certutil -d sql:/home/seluser/.pki/nssdb -A -t "P,," -n localhost -i /usr/local/share/ca-certificates/localhost.crt

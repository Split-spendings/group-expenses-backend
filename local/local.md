## Local development

Steps are described for MACOS. Windows and Linux users please make corresponding adjustments.

### Add lines to /etc/hosts
Windows: c:\Windows\System32\Drivers\etc\hosts

```
127.0.0.1       identity.thesis.net
127.0.0.1       se-back.thesis.net
127.0.0.1       se-doc.thesis.net
```
(After saving the 'hosts' file you can check by pinging the added domain addresses - ping should get response from localhost ip)

### Certificate for https

Generate self-signed certificate

```
openssl req -x509 -sha256 -nodes -newkey rsa:2048 -days 365 -keyout thesis_net.key -out thesis_net.crt -config thesis_net.cnf
```

Import certificate into cacerts keystore. It will force any Java application to trust this certificate. The initial password of the cacerts keystore file is **changeit**.
(This step is necessary only if our back-end is calling other back-end on .thesis.net)
```
sudo keytool -importcert -file thesis_net.crt -alias "*.thesis.net" -cacerts
```
Windows (as admin):
```
keytool -import -keystore "C:\Program Files\Java\jdk-11.0.10\lib\security\cacerts" -file "C:\...\group-expenses-backend\local\thesis_net.crt"
```

Also import it using Keychain Access application and set "Always Trust". It will force browser to trust this certificate.

### NGINX

Install nginx

```
brew install nginx
```
Windows: http://nginx.org/en/docs/windows.html

Copy provided config file

```
cp nginx.conf /usr/local/etc/nginx/nginx.conf
```

Copy certificate files from previous step

```
cp thesis_net.crt /usr/local/etc/nginx/
cp thesis_net.key /usr/local/etc/nginx/
```

Reload nginx config

```
sudo nginx -s reload
```

Or run nginx in docker using commands from nginx-docker/Makefile

### Keycloak

Start keycloak

```
./standalone.sh -Dkeycloak.frontendUrl=https://identity.thesis.net/auth -Djboss.socket.binding.port-offset=100
```

Open keycloak admin site https://identity.thesis.net, find client se-doc and set

```
Valid redirect URIs: https://se-doc.thesis.net/*
Web origins: https://se-doc.thesis.net
```
In the client 'account-console' set Web Origins to '*' both in 'master' and 'thesis' realms

### Backend application

Set next lines in application.properties. These settings correspond to hosts and nginx.conf files.

```
groupexpenses.api-url=https://se-back.thesis.net
server.port=8182
keycloak.auth-server-url=https://identity.thesis.net/auth
```

### Swagger application

Set next lines in application.properties. These settings correspond to hosts and nginx.conf files.

```
server:
  port: 8181

myswagger:
  api-url: https://se-back.thesis.net
  swagger-url: https://se-doc.thesis.net
```

### Android application

We must add lines to hosts file inside Android emulator. It's pretty tricky.

**Important:** install old version of AVD image. Newer versions don't allow make **/system** writable.

```
Android 7.0 (Google APIs) Nougat API 24
or
Android 7.1.1 (Google APIs) Nougat API 25
```

Run emulator with **-writable-system** parameter, for example:

```
%ANDROID_HOME%/sdk/emulator/emulator -avd Pixel_3_XL_API_24 -writable-system
or
$ANDROID_SDK_ROOT/emulator/emulator -avd Pixel_5_API_25 -writable-system -no-snapshot
```
Windows e.g.:
```
C:\Users\abel9\AppData\Local\Android\Sdk\emulator>emulator -avd Pixel_3_XL_API_24 -writable-system
```

Root emulator, make /system dir writable

```
adb root
adb shell
mount -o rw,remount /system
exit
```

Copy hosts file

```
adb push hosts /system/etc/hosts
```
Windows e.g.:
```
C:\...\group-expenses-backend\local>adb push hosts /system/etc/hosts
```

Or just:
```
adb root
adb shell mount -o rw,remount /system
adb push hosts /system/etc/hosts
adb reboot
```

Just check

```
adb shell
cat /system/etc/hosts 
exit
```

Add to AndroidManifest.xml

```
android:networkSecurityConfig="@xml/network_security_config">
```

Create file xml/network_security_config with content

```
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config>
        <domain includeSubdomains="true">thesis.net</domain>
        <trust-anchors>
            <certificates src="@raw/thesis_net"/>
        </trust-anchors>
    </domain-config>
</network-security-config>
```

Copy thesis_net.crt into raw dir

### Important
You need to open each of the addresses in the browser (most importantly back-end's address) and add to exceptions each address because of the self-signed certificate
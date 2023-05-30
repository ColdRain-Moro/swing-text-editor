# TextEditor

- [x] basic text editing
- [x] syntax highlight
- [ ] builtin terminal
- [ ] skining

## Installation

### *nix

~~~shell
./gradlew build
mkdir ~/.config/texteditor
cp ./editor.sh ~/.config/texteditor/editor.sh
cp ./build/libs/TextEditor-1.0-SNAPSHOT-all.jar ~/.config/texteditor/TextEditor.jar
echo "\nalias editor=\"~/.config/texteditor/editor.sh\"\n" >> ~/.zshrc # bash 的话就 .bashrc
source ~/.zshrc
~~~

## UI

![image-20230530153346758](https://persecution-1301196908.cos.ap-chongqing.myqcloud.com/image_bedimage-20230530153346758.png)
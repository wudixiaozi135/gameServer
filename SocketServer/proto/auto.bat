@echo off
cd
pushd .

cd ../src/com/server/bean


del * /Q

popd

protoc.exe --java_out=../src/ client.proto

protoc.exe --java_out=../src/ server.proto
@echo off
echo 测试Fengkong4 API接口...
echo.

echo 1. 健康检查...
curl -X GET http://localhost:8304/api/credit-report/health
echo.
echo.

echo 2. 生成随机征信报文...
curl -X GET http://localhost:8304/api/credit-report/generate/random
echo.
echo.

echo 3. 生成自定义征信报文...
curl -X GET "http://localhost:8304/api/credit-report/generate/custom?creditRecords=3&queryRecords=5"
echo.
echo.

echo API测试完成！
pause

$files = Get-ChildItem "target/surefire-reports/TEST-*.xml"

$total = 0
$failures = 0
$errors = 0
$skipped = 0

foreach ($file in $files) {
    [xml]$report = Get-Content $file.FullName

    $total += [int]$report.testsuite.tests
    $failures += [int]$report.testsuite.failures
    $errors += [int]$report.testsuite.errors
    $skipped += [int]$report.testsuite.skipped
}

$passed = $total - $failures - $errors - $skipped

if ($total -gt 0) {
    $rate = [math]::Round(($passed / $total) * 100, 2)
}
else {
    $rate = 0
}

Write-Host "========== TEST QUALITY SUMMARY =========="
Write-Host "Total Tests: $total"
Write-Host "Passed Tests: $passed"
Write-Host "Failed Tests: $($failures + $errors)"
Write-Host "Skipped Tests: $skipped"
Write-Host "Pass Percentage: $rate percent"
Write-Host "=========================================="
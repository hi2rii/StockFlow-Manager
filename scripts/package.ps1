if (!(Test-Path out)) {
    & "$PSScriptRoot\build.ps1"
}
jar cfm StockFlowManager.jar manifest/MANIFEST.MF -C out .

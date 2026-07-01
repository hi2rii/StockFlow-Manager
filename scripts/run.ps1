if (!(Test-Path out)) {
    & "$PSScriptRoot\build.ps1"
}
java -cp out com.stockflow.Main

# gerar-arvore.ps1

$root = Get-Location
$excludeDirs = @('node_modules', '.next', 'target', '.git')

function Show-Tree {
    param (
        [string]$Path,
        [string]$Prefix = ""
    )

    $items = Get-ChildItem -Path $Path | Where-Object {
        -not ($_.PSIsContainer -and $excludeDirs -contains $_.Name)
    } | Sort-Object { -not $_.PSIsContainer }, Name

    for ($i = 0; $i -lt $items.Count; $i++) {
        $item = $items[$i]
        $isLast = ($i -eq $items.Count - 1)

        if ($isLast) {
            $connector = "+-- "
            $newPrefix = "$Prefix    "
        } else {
            $connector = "|-- "
            $newPrefix = "$Prefix|   "
        }

        "$Prefix$connector$($item.Name)"

        if ($item.PSIsContainer) {
            Show-Tree -Path $item.FullName -Prefix $newPrefix
        }
    }
}

$output = @()
$output += (Split-Path $root -Leaf)
$output += (Show-Tree -Path $root)

$output | Out-File -FilePath "estrutura.txt" -Encoding UTF8

Write-Host "Arquivo estrutura.txt gerado com sucesso!" -ForegroundColor Green

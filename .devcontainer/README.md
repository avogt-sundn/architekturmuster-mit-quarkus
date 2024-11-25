# Setup

Das lokale Setup für Deinen Entwicklungsrechner beginnt mit dem Schritt nach Linux:

- unter Windows installieren wir WSL2 und das Windows Terminal von Microsoft, in WSL2 wählen wir die Ubuntu Distro und installieren `sudo apt install zsh`.
- unter MacOS finden wir bereits eine linux-kompatible Shell mit der `zsh` und ein gutes Terminal-Programm, ergänzen das aber mit `iterm` von [https://iterm2.com/].


## iterm (nur für MacOS)

Am besten installiert man mit `brew`, dem Paketmanager für MacOS.
1. `Terminal.app` öffnen mittels Tastenfolge `CMD+SPACE` `terminal` `ENTER`
2. Darin ausführen:
````bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
# und danach:
brew install --cask iterm2
````

## Oh-my-zsh

Das ` Oh-my-zsh` Projekt erweitert die zsh mit einem Paketmanagement und schönen Voreinstellungen für Prompt und Zeilenvervollständingung.

## Powerlevel10k

````bash
# gh cli
gh repo clone romkatv/powerlevel10k $ZSH_CUSTOM/themes/powerlevel10k

# git
git clone https://github.com/romkatv/powerlevel10k.git $ZSH_CUSTOM/themes/powerlevel10k
````

### Powerlevel10k Fonts in VS Code

MacOS: [~/Library/Application Support/Code/User/settings.json]()

    "terminal.integrated.fontFamily": "'Source Code Pro for Powerline', 'Hack Nerd Font'",


## Links

- powerlevel10k einrichten:
  - <https://dev.to/abdfnx/oh-my-zsh-powerlevel10k-cool-terminal-1no0>
  -

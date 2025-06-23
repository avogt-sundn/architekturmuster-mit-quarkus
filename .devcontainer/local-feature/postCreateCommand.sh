#!/bin/bash
echo "Setting up zsh and powerlevel10k for ${_REMOTE_USER}"
export ZSH_CUSTOM=/home/${_REMOTE_USER}/.oh-my-zsh/custom;
git clone https://github.com/romkatv/powerlevel10k.git $ZSH_CUSTOM/themes/powerlevel10k
sed -i 's;^ZSH_THEME=".*"$;ZSH_THEME="powerlevel10k/powerlevel10k";' /home/${_REMOTE_USER}/.zshrc

echo '[[ ! -f /home/${_REMOTE_USER}/.p10k.zsh ]] || source /home/${_REMOTE_USER}/.p10k.zsh' >> /home/${_REMOTE_USER}/.zshrc

cp .p10k.zsh /home/${_REMOTE_USER}/.p10k.zsh
echo "Done setting up zsh and powerlevel10k for ${_REMOTE_USER}"
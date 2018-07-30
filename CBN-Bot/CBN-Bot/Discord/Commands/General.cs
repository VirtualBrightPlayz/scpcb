using Discord.Commands;
using System;
using System.Threading.Tasks;
using Discord;

namespace CBN_Bot
{
    public class General : JModuleBase
    {
        [Command("ping")]
        public async Task Ping() => await ReplyAsync("Pong.");

        [RequireRole("CBN Lead")]
        [Command("secret")]
        public async Task SecretPing() => await ReplyAsync("Secret pong.");

        [RequireRole("CBN Lead")]
        [Command("stfu"), Alias("kys", "die")]
        public async Task Die()
        {
            await ReplyAsync("Going offline.");
            await CBN.Instance.Client.SetStatusAsync(Discord.UserStatus.Invisible);

            Environment.Exit(0);
        }
    }
}

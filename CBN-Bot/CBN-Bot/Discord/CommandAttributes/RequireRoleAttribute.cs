using System;
using System.Threading.Tasks;
using Discord.Commands;
using Discord;

namespace CBN_Bot
{
    /// <summary>
    /// Require that the command or group invoked is done by a user with a specific role.
    /// </summary>
    [AttributeUsage(AttributeTargets.All, AllowMultiple = false)]
    class RequireRoleAttribute : PreconditionAttribute
    {
        private string roleName;
        public RequireRoleAttribute(string roleName)
        {
            this.roleName = roleName;
        }

        public override async Task<PreconditionResult> CheckPermissions(ICommandContext context, CommandInfo command, IServiceProvider services)
        {
            foreach (IRole role in CBN.Instance.CBNGuild.Roles)
            {
                if (roleName == role.Name)
                {
                    IGuildUser user = await CBN.Instance.CBNGuild.GetUserAsync(context.User.Id);
                    foreach (ulong roleID in user.RoleIds)
                    {
                        if (role.Id == roleID)
                        {
                            return PreconditionResult.FromSuccess();
                        }
                    }
                }
            }


            return PreconditionResult.FromError(context.User + "does not have the \"" + roleName + "\" role.");
        }
    }
}
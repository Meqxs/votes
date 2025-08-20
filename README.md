# Votes Plugin

A Minecraft Paper plugin for creating and managing server-wide votes.

## Features

- **Vote Creation**: Server operators can create votes with custom titles and up to 5 options
- **Color Support**: Use Minecraft color codes (e.g., `&b&lBlue Bold Text`) in vote titles and options
- **Interactive GUI**: Players can vote through a clean inventory-based interface
- **One Vote Per Round**: Players can only vote once per voting session
- **Real-time Results**: View current vote counts and percentages
- **Permission System**: Proper permission checks for vote creation and voting

## Installation

1. Download the compiled JAR file
2. Place it in your server's `plugins` folder
3. Restart your server
4. The plugin will be automatically enabled

## Commands

### `/votes` (OP only)
- **Permission**: `votes.create` (default: OP)
- **Description**: Starts the vote creation process
- **Usage**: Type `/votes` and follow the prompts

### `/vote`
- **Permission**: `votes.vote` (default: true)
- **Description**: Opens the voting GUI for the current active vote
- **Usage**: `/vote`

## How to Create a Vote

1. **Start Creation**: Type `/votes` (requires OP)
2. **Enter Title**: Type the vote title (supports color codes like `&b&l`)
3. **Set Options Count**: Enter a number between 1-5 for how many options
4. **Add Options**: Type each option one by one
5. **Complete**: The vote will be announced to all players

## How to Vote

1. **See Announcement**: When a vote is created, all players see an announcement
2. **Open GUI**: Type `/vote` or click the link in chat
3. **Select Option**: Click on your preferred option in the GUI
4. **View Results**: See the current vote counts and percentages

## Color Codes

You can use Minecraft color codes in vote titles and options:
- `&a` - Green
- `&b` - Aqua
- `&c` - Red
- `&d` - Light Purple
- `&e` - Yellow
- `&f` - White
- `&l` - Bold
- `&n` - Underline
- `&o` - Italic
- `&k` - Obfuscated

Example: `&b&lWhat should we build next?`

## Permissions

- `votes.create` - Allows creating votes (default: OP)
- `votes.vote` - Allows voting (default: true)

## Technical Details

- **Minecraft Version**: 1.21+
- **Paper API**: 1.21.1-R0.1-SNAPSHOT
- **Java Version**: 17+
- **Author**: MeqxsDev

## Building from Source

1. Clone the repository
2. Run `mvn clean package`
3. Find the JAR file in the `target` folder

## Support

For issues or questions, please contact the plugin author.

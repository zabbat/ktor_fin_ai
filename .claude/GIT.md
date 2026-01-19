# Git Guidelines for Agents

## Pushing to Remote

**IMPORTANT**: Agents must always ask for user confirmation before pushing any changes to a remote repository.

### Rules

1. **Always ask before `git push`** - Never push to any remote branch without explicit user approval
2. **Describe what will be pushed** - Before asking, show:
   - The branch name
   - Number of commits
   - Summary of changes
3. **Wait for confirmation** - Only proceed with the push after the user explicitly approves

### Example

Before pushing, agents should ask:

```
I'm ready to push the following to origin/main:
- 3 commits
- Added user authentication feature
- Fixed database connection bug

Should I proceed with the push?
```

## Other Git Operations

- **Commits**: Agents can create commits without asking (unless configured otherwise)
- **Branches**: Agents can create and switch branches locally without asking
- **Pull/Fetch**: Agents can pull and fetch without asking
- **Force push**: Never use `--force` without explicit user request and confirmation

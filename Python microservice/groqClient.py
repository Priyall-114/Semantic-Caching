import os

from dotenv import load_dotenv
from groq import Groq

load_dotenv()  # reads .env into os.environ

client = Groq(api_key=os.environ["GROQ_API_KEY"])  # created once, fails fast if missing


def groq_call(query):
    chat_completion = client.chat.completions.create(
        messages=[{"role": "user", "content": query}],
        model="openai/gpt-oss-120b",
    )
    return chat_completion.choices[0].message.content
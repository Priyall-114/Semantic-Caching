from fastapi import FastAPI
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer
from groqClient import groq_call


app=FastAPI()
model = SentenceTransformer("all-MiniLM-L6-v2")

class EmbedRequest(BaseModel):
    text:str

class LLMRequest(BaseModel):
    query: str

@app.get("/health")
def health():
    return {"status": "ok"}

@app.post("/embed")
def embed(request:EmbedRequest):
    vector=model.encode(request.text).tolist()
    return {"embedding":vector}#Received embeddings for the query and returned it


@app.post("/LLM")
def LLM(request: LLMRequest):
    response=groq_call(request.query)
    return {"answer": response}
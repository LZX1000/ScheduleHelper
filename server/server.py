# server.py

from flask import Flask
from flask_restful import Resource, Api
from flask_cors import CORS


from api.swen_344_db_utils import *

app = Flask(__name__) #create Flask instance
CORS(app)

api = Api(app) #api router

if __name__ == '__main__':
    print("Starting flask")
    app.run(debug=True), #starts Flask

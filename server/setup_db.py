# setup_db.py
import yaml, subprocess

with open('config/db.yml') as f:
    cfg = yaml.safe_load(f)

subprocess.run([
    'psql',
    '-U', cfg['username'],
    '-d', cfg['database'],
    '-h', cfg['host'],
    '-f', 'server/schema.sql'
])

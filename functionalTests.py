import urllib.request
import json
from pprint import pprint

hostname = "localhost"
port = "8081"
search_id_url = f"http://{hostname}:{port}/api/contact/search/id"
search_name_url = f"http://{hostname}:{port}/api/contact/search/name"
add_contact_url = f"http://{hostname}:{port}/api/contact/add"
headers = {
    "Content-Type": "application/json",
    "Accept": "application/json",
}


def add_new_contact(contact_name):
    values = {
    "name": contact_name,
    }
    data = json.dumps(values).encode("utf-8")
    try:
        req = urllib.request.Request(add_contact_url, data, headers)
        with urllib.request.urlopen(req) as f:
            res = f.read()
        pprint(res.decode())
    except Exception as e:
        print("error")
        pprint(e)


def search_by_id(id):
    values = {
    "id": id,
    }
    data = json.dumps(values).encode("utf-8")
    try:
        req = urllib.request.Request(search_id_url, data, headers)
        with urllib.request.urlopen(req) as f:
            res = f.read()
        pprint(res.decode())
    except Exception as e:
        print("error")
        pprint(e)


def search_by_name(searchQueary):
    values = {
    "name": searchQueary,
    }
    data = json.dumps(values).encode("utf-8")
    try:
        req = urllib.request.Request(search_name_url, data, headers)
        with urllib.request.urlopen(req) as f:
            res = f.read()
        pprint(res.decode())
    except Exception as e:
        print("error")
        pprint(e)

add_new_contact("David Christy")
add_new_contact("Bruce Wayne")
add_new_contact("Batman")
add_new_contact("Joker")
add_new_contact("Dick Grayson")
search_by_id(1)
search_by_id(2)
search_by_id(6)
search_by_name("da")
search_by_name("david")
search_by_name("bru")
search_by_name("Bruce")
search_by_name("Wayne")
search_by_name("Bruce Wayne")
search_by_name("")
search_by_name("ZZZ")